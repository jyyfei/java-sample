package com.sample.classloader.classNotInit;

/**
 * @author yunfei.jyf
 * @date 2024/3/29
 */
public class SubClass extends SuperClass {
    static {
        System.out.println("sub hello");
    }
}
