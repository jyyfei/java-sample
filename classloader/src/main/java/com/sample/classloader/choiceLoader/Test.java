package com.sample.classloader.choiceLoader;

import com.sample.classloader.ClassUtil;
import sun.misc.Launcher;

import java.lang.reflect.Method;
import java.net.URLClassLoader;

/**
 * 虚拟机类加载使用哪个类加载器跟TCCL没关系，因为把TCCL改掉还是用的AppClassLoader
 * 虚拟机使用哪个类加载？目前来看和Class.forName的逻辑是一样的，就是看调用方的类加载器一致
 * 之所以切换TCCL是为了在spi等场景先也用自己指定的Classloader
 *
 * @author yunfei.jyf
 * @date 2024/7/31
 */
public class Test {
    public static Class clazz = TestAA.class;

    public static void main(String[] args) throws Exception {
        // sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println("默认Classloader，静态字段的Classloader：" + Test.clazz.getClassLoader());
        Thread thread = new Thread(() -> {
            // 创建自定义Classloader，parent是extClassloader
            URLClassLoader myAppClassloader = ClassUtil
                    .newMyAppClassloader(Launcher.getLauncher().getClassLoader().getParent());

            // 切换TCCL为自定义的Classloader
            ClassLoader old = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(myAppClassloader);
            try {
                // 场景1、只切换Classloader异步线程执行，不管是直接执行还是反射执行，test中加载TestAA使用appClassLoader
                // Test.test();

                // 场景2、test中加载TestAA使用myAppClassloader，即使不切换TCCL也是myAppClassloader
                // Thread.currentThread().setContextClassLoader(old);
                Class<?> clazz = myAppClassloader.loadClass(Test.class.getName());
                System.out.println(clazz.equals(Test.class));

                // 场景3、test中加载TestAA使用old
                // Class<?> clazz = old.loadClass(Test.class.getName());

                // 反射执行
                Method testMethod = clazz.getDeclaredMethod("test");
                testMethod.invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                Thread.currentThread().setContextClassLoader(old);
            }
        });

        thread.start();
    }

    public static void test() throws Exception {
        System.out.println(Thread.currentThread().getContextClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());

        // TCCL切换成自定义的
//        Thread.currentThread().setContextClassLoader(ClassUtil.newMyAppClassloader(sun.misc.Launcher.getLauncher().getClassLoader().getParent()));
//        System.out.println(Thread.currentThread().getContextClassLoader());
        // Class.forName使用调用方类com.sample.classloader.choiceLoader.Test的Classloader来加载TestAA
//        System.out.println(Class.forName(TestAA.class.getName()).getClassLoader());

        TestAA testAA = new TestAA();
        System.out.println(testAA);
        System.out.println(testAA.getClass().getClassLoader());
        System.out.println(TestAA.class.getClassLoader());
        System.out.println(Test.clazz.equals(testAA.getClass()));
    }
}
