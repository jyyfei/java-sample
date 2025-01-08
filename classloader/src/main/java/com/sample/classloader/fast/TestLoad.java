package com.sample.classloader.fast;

/**
 * @author yunfei.jyf
 * @date 2024/11/18
 */
public class TestLoad {
    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader.getClass().getName());
        try {
            long start = System.currentTimeMillis();
            Class<?> testClass = classLoader.loadClass("com.sample.classloader.fast.TestLoad");
            for (int i = 0; i < 100; i++) {
                testClass = classLoader.loadClass("com.sample.classloader.fast.TestLoad");
                testClass = classLoader.loadClass("com.sample.classloader.fast.TestLoad");
            }
            System.out.println(testClass.getName());
            System.out.println(String.valueOf(System.currentTimeMillis() - start));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
