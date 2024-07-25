package com.sample.javassist;

/**
 * @author yunfei.jyf
 * @date 2024/5/13
 */
public class TestClass {
    public TestClass() {
        System.out.println("TestClass()");
    }

    public TestClass(int aa) {
        System.out.println("TestClass(aa)");
    }

    public boolean test() {
        System.out.println("test()");
        return true;
    }
}
