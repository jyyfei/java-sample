package com.sample.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yunfei.jyf
 * @date 2024/4/11
 */
public class TransmittableThreadLocalTest {
    public static void main2(String[] args) throws InterruptedException {
        TransmittableThreadLocal<String> transmittableThreadLocal = new TransmittableThreadLocal<>();
        transmittableThreadLocal.set("a");

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        //TTL 干的事情就是在 任务创建的时候用复制此时的父线程的threadLocal，在任务执行完毕后，回滚回去
        //TTL 对于InheritableThreadLocal的扩充是，InheritableThreadLocal在线程创建的时候，才会拷贝此时父线程的threadLocal,
        // 线程池线程里面同一个线程会有脏数据，这样的话，就不会变化了
        TtlExecutors.getTtlExecutor(executorService).execute(() -> {
            System.out.println("初始拷贝：" + transmittableThreadLocal.get());
            TransmittableThreadLocal<String> transmittableThreadLocal1 = new TransmittableThreadLocal<>();
            transmittableThreadLocal.set("a2");
            transmittableThreadLocal1.set("c");
            System.out.println("修改后：" + transmittableThreadLocal.get());
            System.out.println("子线程本地变量：" +transmittableThreadLocal1.get());

        });
        Thread.sleep(1000);
        TtlExecutors.getTtlExecutor(executorService).execute(() -> {
            System.out.println("初始拷贝：" + transmittableThreadLocal.get());
        });
        Thread.sleep(1000);

        transmittableThreadLocal.set("a2");
        TtlExecutors.getTtlExecutor(executorService).execute(() -> {
            System.out.println("父修改后初始拷贝：" + transmittableThreadLocal.get());
        });
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 是引用拷贝
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        TransmittableThreadLocal<Bean> transmittableThreadLocal = new TransmittableThreadLocal<>();
        Bean aBean = new Bean("a");
        transmittableThreadLocal.set(aBean);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        TtlExecutors.getTtlExecutor(executorService).execute(() -> {
            System.out.println("初始拷贝：" + transmittableThreadLocal.get());
            Bean bean = transmittableThreadLocal.get();
            bean.setName("a1");
        });

        Thread.sleep(1000);
        TtlExecutors.getTtlExecutor(executorService).execute(() -> System.out.println("初始拷贝：" + transmittableThreadLocal.get()));
    }
}
