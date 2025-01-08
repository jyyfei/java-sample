package com.sample.classloader.fast.fast;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

/**
 * Simple Fast URLCLassLoader -Dc2f.boot.turbo.classloader.enable=true to enable it Not support addUrl
 * Only support file (directory or jar) protocol Url
 */
public class FastURLClassLoader extends URLClassLoader implements Closeable {

  private static final String ENABLE_KEY = "c2f.boot.turbo.classloader.enable";

  private static final URL[] EMPTY_URLS = new URL[0];

  private static final boolean ENABLE = "true".equals(System.getProperty(ENABLE_KEY));

  private final boolean enable;

  /* The search path for classes and resources */
  private final FastURLClassPath ucp;

  public FastURLClassLoader(URL[] urls, ClassLoader parent) {
    this(urls, parent, true);
  }

//    public FastURLClassLoader(URL[] urls) {
//        this(urls, ENABLE);
//    }

  public FastURLClassLoader(URL[] urls, ClassLoader parent, boolean enable) {
    super(enable ? EMPTY_URLS : urls, parent);
    this.enable = true;
    this.ucp = enable ? new FastURLClassPath(urls) : null;
  }

//    public FastURLClassLoader(URL[] urls, boolean enable) {
//        super(enable ? EMPTY_URLS : urls);
//        this.enable = enable;
//        this.ucp = enable ? new FastURLClassPath(urls) : null;
//    }

  @Override
  protected void addURL(URL url) {
    if (enable) {
      ucp.addURL(url);
    } else {
      super.addURL(url);
    }
  }

  @Override
  public URL[] getURLs() {
    return enable ? ucp.getURLs() : super.getURLs();
  }

  public void close() throws IOException {
    IOException firstException = null;
    try {
      super.close();
    } catch (NoSuchMethodError ignore) {
      // Running on Java 6. Continue.
    } catch (IOException e) {
      firstException = e;
    }

    List<IOException> errors = ucp.closeLoaders();
    if (!errors.isEmpty()) {
      if (firstException == null) {
        firstException = errors.remove(0);
      }
      // Suppress any remaining exceptions
      try {
        for (IOException error : errors) {
          firstException.addSuppressed(error);
        }
      } catch (NoSuchMethodError ignore) {
        // Running on Java 6. Continue.
      }
    }
    if (firstException != null) {
      throw firstException;
    }
  }

  public Set<String> getIndexKeys() {
    if (enable) {
      return ucp.getIndexKeys();
    } else {
      throw new IllegalStateException("Not support getIndexKeys!");
    }
  }

  @Override
  protected Class<?> findClass(final String name) throws ClassNotFoundException {
    if (!enable) {
      return super.findClass(name);
    }

    String path = name.replace('.', '/').concat(".class");
    FastResource res = ucp.getResource(path, false);
    if (res != null) {
      try {
        return defineClass(name, res);
      } catch (IOException e) {
        throw new ClassNotFoundException(name, e);
      }
    } else {
      throw new ClassNotFoundException(name);
    }
  }

  /*
   * Retrieve the package using the specified package name.
   * If non-null, verify the package using the specified code
   * source and manifest.
   */
  private Package getAndVerifyPackage(String pkgname, Manifest man, URL url) {
    Package pkg = getPackage(pkgname);
    if (pkg != null) {
      // Package found, so check package sealing.
      if (pkg.isSealed()) {
        // Verify that code source URL is the same.
        if (!pkg.isSealed(url)) {
          throw new SecurityException("sealing violation: package " + pkgname + " is sealed");
        }
      } else {
        // Make sure we are not attempting to seal the package
        // at this code source URL.
        if ((man != null) && isSealed(pkgname, man)) {
          throw new SecurityException(
              "sealing violation: can't seal package " + pkgname + ": already loaded");
        }
      }
    }
    return pkg;
  }

  // Also called by VM to define Package for classes loaded from the CDS
  // archive
  private void definePackageInternal(String pkgname, Manifest man, URL url) {
    if (getAndVerifyPackage(pkgname, man, url) == null) {
      try {
        if (man != null) {
          definePackage(pkgname, man, url);
        } else {
          definePackage(pkgname, null, null, null, null, null, null, null);
        }
      } catch (IllegalArgumentException iae) {
        // parallel-capable class loaders: re-verify in case of a
        // race condition
        if (getAndVerifyPackage(pkgname, man, url) == null) {
          // Should never happen
          throw new AssertionError("Cannot find package " + pkgname);
        }
      }
    }
  }

  /*
   * Defines a Class using the class bytes obtained from the specified
   * Resource. The resulting Class must be resolved before it can be
   * used.
   */
  private Class<?> defineClass(String name, FastResource res) throws IOException {
    int i = name.lastIndexOf('.');
    URL url = res.getCodeSourceURL();
    if (i != -1) {
      String pkgname = name.substring(0, i);
      // Check if package already loaded.
      Manifest man = res.getManifest();
      definePackageInternal(pkgname, man, url);
    }
    // Now read the class bytes and define the class
    java.nio.ByteBuffer bb = res.getByteBuffer();
    if (bb != null) {
      // Use (direct) ByteBuffer:
      CodeSigner[] signers = res.getCodeSigners();
      CodeSource cs = new CodeSource(url, signers);
      return defineClass(name, bb, cs);
    } else {
      byte[] b = res.getBytes();
      // must read certificates AFTER reading bytes.
      CodeSigner[] signers = res.getCodeSigners();
      CodeSource cs = new CodeSource(url, signers);
      return defineClass(name, b, 0, b.length, cs);
    }
  }

  /*
   * Returns true if the specified package name is sealed according to the
   * given manifest.
   *
   * @throws SecurityException if the package name is untrusted in the manifest
   */
  private boolean isSealed(String name, Manifest man) {
    String path = name.replace('.', '/').concat("/");
    Attributes attr = man.getAttributes(path);
    String sealed = null;
    if (attr != null) {
      sealed = attr.getValue(Name.SEALED);
    }
    if (sealed == null) {
      if ((attr = man.getMainAttributes()) != null) {
        sealed = attr.getValue(Name.SEALED);
      }
    }
    return "true".equalsIgnoreCase(sealed);
  }

  /**
   * Finds the resource with the specified name on the URL search path.
   *
   * @param name the name of the resource
   * @return a {@code URL} for the resource, or {@code null} if the resource could not be found, or
   * if the loader is closed.
   */
  @Override
  public URL findResource(final String name) {
    /*
     * The same restriction to finding classes applies to resources
     */
    if (enable) {
      return ucp.findResource(name, true);
    } else {
      return super.findResource(name);
    }
  }

  @Override
  public Enumeration<URL> findResources(final String name) throws IOException {
    if (!enable) {
      return super.findResources(name);
    }

    final Enumeration<URL> e = ucp.findResources(name, true);

    return new Enumeration<URL>() {
      private URL url = null;

      private boolean next() {
        if (url != null) {
          return true;
        }
        do {
          URL u = e.hasMoreElements() ? e.nextElement() : null;
          if (u == null) {
            break;
          }
          url = u;
        } while (url == null);
        return url != null;
      }

      public URL nextElement() {
        if (!next()) {
          throw new NoSuchElementException();
        }
        URL u = url;
        url = null;
        return u;
      }

      public boolean hasMoreElements() {
        return next();
      }
    };
  }

  static {
    try {
      ClassLoader.registerAsParallelCapable();
    } catch (NoSuchMethodError ignore) {
      // Running on Java 6. Continue.
    }
  }
}
