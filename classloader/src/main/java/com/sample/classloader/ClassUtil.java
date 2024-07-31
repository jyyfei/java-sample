package com.sample.classloader;

import sun.net.www.ParseUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;

/**
 * @author yunfei.jyf
 * @date 2024/4/22
 */
public class ClassUtil {
    public static URLClassLoader newMyAppClassloader(ClassLoader parent) {
        final String var1 = System.getProperty("java.class.path");
        final File[] var2 = var1 == null ? new File[0] : getClassPath(var1);
        URL[] var1x = var1 == null ? new URL[0] : pathToURLs(var2);
        return new URLClassLoader(var1x, parent);
    }

    private static File[] getClassPath(String var0) {
        File[] var1;
        if (var0 != null) {
            int var2 = 0;
            int var3 = 1;
            boolean var4 = false;

            int var5;
            int var7;
            for (var5 = 0; (var7 = var0.indexOf(File.pathSeparator, var5)) != -1; var5 = var7 + 1) {
                ++var3;
            }

            var1 = new File[var3];
            var4 = false;

            for (var5 = 0; (var7 = var0.indexOf(File.pathSeparator, var5)) != -1; var5 = var7 + 1) {
                if (var7 - var5 > 0) {
                    var1[var2++] = new File(var0.substring(var5, var7));
                } else {
                    var1[var2++] = new File(".");
                }
            }

            if (var5 < var0.length()) {
                var1[var2++] = new File(var0.substring(var5));
            } else {
                var1[var2++] = new File(".");
            }

            if (var2 != var3) {
                File[] var6 = new File[var2];
                System.arraycopy(var1, 0, var6, 0, var2);
                var1 = var6;
            }
        } else {
            var1 = new File[0];
        }

        return var1;
    }

    private static URL[] pathToURLs(File[] var0) {
        URL[] var1 = new URL[var0.length];

        for (int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = getFileURL(var0[var2]);
        }

        return var1;
    }

    private static URL getFileURL(File var0) {
        try {
            var0 = var0.getCanonicalFile();
        } catch (IOException var3) {
        }

        try {
            return ParseUtil.fileToEncodedURL(var0);
        } catch (MalformedURLException var2) {
            throw new InternalError(var2);
        }
    }

    public static URLClassLoader newClassloader(String path) throws MalformedURLException {
        URL[] urls = {
                new File(path).toURI().toURL()
        };
        return new URLClassLoader(urls);
    }

    public static Class reDefine(Class clazz, ClassLoader classLoader) throws Exception {
        Method mdDefineClass = ClassLoader.class
                .getDeclaredMethod("defineClass",
                        String.class, byte[].class, int.class, int.class,
                        ProtectionDomain.class);
        mdDefineClass.setAccessible(true);

        byte[] classFileBuffer = ClassUtil.getClassFileBuffer(clazz);
        Class c1 = (Class) mdDefineClass.invoke(classLoader, new Object[]{
                clazz.getName(), classFileBuffer, 0, classFileBuffer.length, clazz.getProtectionDomain()
        });
        return c1;
    }

    public static byte[] getClassFileBuffer(Class<?> clazz) throws IOException {
        String classFilePath = clazz.getName().replace('.', '/') + ".class";
        try (InputStream is = clazz.getClassLoader().getResourceAsStream(classFilePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }
}
