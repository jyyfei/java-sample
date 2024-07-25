package com.sample.classloader.staticUse;

/**
 * @author yunfei.jyf
 * @date 2024/4/22
 */
public class StaticTestClass {
    public static String hello() {
        System.out.println(StaticTestClass.class.getClassLoader());
        return StaticTestClass.class.getClassLoader().toString();
    }

    public void see() {
        System.out.println("this:" + this.getClass().getClassLoader());
    }
}
