package com.sample.classloader.staticUse;

import com.sample.classloader.ClassUtil;

import java.net.URLClassLoader;

/**
 * 使用类的静态方法时会用哪个ClassLoader的类
 * <p>
 * 假设ClassLoader1和ClassLoader2都加载了SequenceGenerateUtil这个类，
 * 代码执行到SequenceGenerateUtil.nextId(tableName)时用哪个？？？
 * 目前来看StaticTestClass用哪个类加载器是根据StaticUseTest用哪个类加载器来决定的
 *
 * @author yunfei.jyf
 * @date 2024/4/22
 */
public class StaticUseTest {
    public static void main(String[] args) throws Exception {
        URLClassLoader classLoader1 = ClassUtil.newClassloader("/tmp/");
        System.out.println(classLoader1);
        URLClassLoader classLoader2 = ClassUtil.newClassloader("/tmp/");
        System.out.println(classLoader2);
        Class reDefine1 = ClassUtil.reDefine(StaticTestClass.class, classLoader1);
        System.out.println(reDefine1.getClassLoader());
        Class reDefine2 = ClassUtil.reDefine(StaticTestClass.class, classLoader2);
        System.out.println(reDefine2.getClassLoader());
        Object o2 = reDefine2.newInstance();

        System.out.println("0:" + Thread.currentThread().getContextClassLoader());
        StaticTestClass.hello();

        Thread.currentThread().setContextClassLoader(classLoader1);
        System.out.println("1:" + Thread.currentThread().getContextClassLoader());
        StaticTestClass.hello();

        Thread.currentThread().setContextClassLoader(classLoader2);
        System.out.println("2:" + Thread.currentThread().getContextClassLoader());
        StaticTestClass.hello();

        System.out.println("======================");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread 1:" + Thread.currentThread().getContextClassLoader());
                StaticTestClass staticTestClass = new StaticTestClass();
                staticTestClass.see();

                StaticTestClass.hello();
            }
        });
        thread1.setContextClassLoader(classLoader1);
        thread1.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread 2:" + Thread.currentThread().getContextClassLoader());
                StaticTestClass.hello();
            }
        });
        thread2.setContextClassLoader(classLoader2);
        thread2.start();
    }
}
