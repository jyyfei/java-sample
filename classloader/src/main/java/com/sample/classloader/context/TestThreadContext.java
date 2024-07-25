package com.sample.classloader.context;

import com.sample.classloader.ClassUtil;

import java.net.MalformedURLException;
import java.net.URLClassLoader;

/**
 * @author yunfei.jyf
 * @date 2024/5/21
 */
public class TestThreadContext {
    public static void main(String[] args) throws MalformedURLException {
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getContextClassLoader());
        URLClassLoader urlClassLoader = ClassUtil.newClassloader("/test");
        System.out.println(urlClassLoader);
        Thread.currentThread().setContextClassLoader(urlClassLoader);
        System.out.println(Thread.currentThread().getContextClassLoader());


        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getContextClassLoader());
        }).start();
    }
}
