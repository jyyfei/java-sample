package com.sample.classloader.whenLoad;

/**
 * 只有逻辑真正用到某个类时才会触发这个类加载
 *
 * 通过断点sun.misc.Launcher.AppClassLoader#loadClass(java.lang.String, boolean)
 * 可以看到TestAA.getInstance()只加载了TestAA自己
 *
 * @author yunfei.jyf
 * @date 2024/5/7
 */
public class TestRun {
    public static void main(String[] args) {
        System.out.println(TestAA.getInstance());
    }
}
