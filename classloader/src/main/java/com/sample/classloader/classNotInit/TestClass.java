package com.sample.classloader.classNotInit;

/**
 * @author yunfei.jyf
 * @date 2024/3/29
 */
public class TestClass {
    public static void main(String[] args) {
        // 通过数组定义引用类，会触发类的加载
        SuperClass[] aa = new SuperClass[10];
        // 子类引用父类的静态方法不会导致子类初始化，但是会触发子类的加载
        //System.out.println(SubClass.value);
        System.out.println("qqq");
    }
}
