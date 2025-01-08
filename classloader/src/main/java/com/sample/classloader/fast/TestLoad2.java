package com.sample.classloader.fast;

import com.sample.classloader.fast.fast.FastURLClassLoader;
import org.reflections.util.ClasspathHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

/**
 * @author yunfei.jyf
 * @date 2024/11/18
 */
public class TestLoad2 {
    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Collection<URL> urls = ClasspathHelper.forPackage("", classLoader);
        FastURLClassLoader fastURLClassLoader = new FastURLClassLoader(urls.toArray(new URL[0]), classLoader);
        System.out.println(fastURLClassLoader.getClass().getName());
        try {
            long start = System.currentTimeMillis();
            Class<?> testClass = fastURLClassLoader.loadClass("com.sample.classloader.fast.TestLoad2");
            for (int i = 0; i < 100; i++) {
                testClass = fastURLClassLoader.loadClass("com.sample.classloader.fast.TestLoad2");
                testClass = fastURLClassLoader.loadClass("com.sample.classloader.fast.TestLoad2");
            }
            System.out.println(testClass.getName());
            System.out.println(String.valueOf(System.currentTimeMillis() - start));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL forClass(Class<?> aClass, ClassLoader... classLoaders) {
        final ClassLoader[] loaders = classLoaders(classLoaders);
        final String resourceName = aClass.getName().replace(".", "/") + ".class";
        for (ClassLoader classLoader : loaders) {
            try {
                final URL url = classLoader.getResource(resourceName);
                if (url != null) {
                    final String normalizedUrl = url.toExternalForm().substring(0, url.toExternalForm().lastIndexOf(aClass.getPackage().getName().replace(".", "/")));
                    return new URL(normalizedUrl);
                }
            } catch (MalformedURLException e) {
            }
        }
        return null;
    }

    public static ClassLoader[] classLoaders(ClassLoader... classLoaders) {
        if (classLoaders != null && classLoaders.length != 0) {
            return classLoaders;
        } else {
            ClassLoader contextClassLoader = contextClassLoader();
            return new ClassLoader[]{contextClassLoader};

        }
    }

    public static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
