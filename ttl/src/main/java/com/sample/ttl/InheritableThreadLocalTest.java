package com.sample.ttl;

/**
 * InheritableThreadLocal可继承的ThreadLocal，可以跨线程之间传递值
 * 原理：new Thread（）→ init()  → ThreadLocal.createInheritedMap(）→ new ThreadLocalMap(); 在线程创建的时候把父对象的inheritableThreadLocals字段拷贝到子对象里面；默认是引用拷贝，可以重写
 * 局限：现在大多数用的都是线程池，线程只会创建一次；这样的话线程InheritableThreadLocal的值就不会恢复了，各个任务用的是同一个InheritableThreadLocal了
 *
 * ThreadLocal内如何使用线性探测解决hash冲突？？？
 * @author yunfei.jyf
 * @date 2024/4/11
 */
public class InheritableThreadLocalTest {
    public static void main(String[] args) {
        ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        inheritableThreadLocal.set("hello");

        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("world");

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(inheritableThreadLocal.get());
                System.out.println(threadLocal.get());
            }
        }).start();
    }
}
