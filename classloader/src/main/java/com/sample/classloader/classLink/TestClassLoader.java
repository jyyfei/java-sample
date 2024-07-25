package com.sample.classloader.classLink;

import com.sample.classloader.ClassUtil;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @author yunfei.jyf
 * @date 2024/6/24
 */
public class TestClassLoader extends URLClassLoader {

    public TestClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public TestClassLoader(URL[] urls) {
        super(urls);
    }

    public TestClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> aClass = super.loadClass(name, resolve);
        if (name.startsWith("java.") || name.contains("TestBB")) {
            return aClass;
        }
        try {
            return ClassUtil.reDefine(aClass, this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
