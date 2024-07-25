package com.sample.classloader.whenLoad;

/**
 * 只有逻辑真正用到某个类时才会触发这个类加载
 *
 * @author yunfei.jyf
 * @date 2024/5/7
 */
public class TestRun {
    public static void main(String[] args) {
        System.out.println(TestAA.getInstance());
    }
}
