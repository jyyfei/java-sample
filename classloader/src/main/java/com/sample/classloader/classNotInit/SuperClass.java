package com.sample.classloader.classNotInit;

/**
 * @author yunfei.jyf
 * @date 2024/3/29
 */
public class SuperClass {
    static {
        System.out.println("super hello");
    }
    public static int value = 123;
}
