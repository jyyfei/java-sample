package com.sample.classloader.classLink;

import com.sample.classloader.whenLoad.TestCC;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * @author yunfei.jyf
 * @date 2024/6/24
 */
public class TestRun {

    public static void main(String[] args) throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        URL[] urls = {new File("/tmp/").toURI().toURL()};
        TestClassLoader testClassLoader = new TestClassLoader(urls, contextClassLoader);
        Thread.currentThread().setContextClassLoader(testClassLoader);


        Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass("com.sample.classloader.classLink.TestRun");
        Object object = aClass.newInstance();
        Method test = aClass.getDeclaredMethod("test", null);
        test.invoke(object);

        TestClassLoader testClassLoader2 = new TestClassLoader(urls, contextClassLoader);
        Thread.currentThread().setContextClassLoader(testClassLoader2);

        Class<?> aClass2 = Thread.currentThread().getContextClassLoader().loadClass("com.sample.classloader.classLink.TestRun");
        Object object2 = aClass2.newInstance();
        Method test2 = aClass2.getDeclaredMethod("test", null);
        test2.invoke(object2);
    }

    public void test() {
        TestCC testCC2 = new TestCC();
        System.out.println(testCC2.getClass().getClassLoader());
        System.out.println(testCC2.getTestBB().getClass().getClassLoader());
    }
}
