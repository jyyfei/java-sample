package com.sample.threadpool.parallelStream;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

/**
 * @author yunfei.jyf
 * @date 2024/9/26
 */
public class ParallelStreamTest {
    public static void main(String[] args) {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        URLClassLoader urlClassLoaderA = new URLClassLoader(new URL[]{});
        URLClassLoader urlClassLoaderB = new URLClassLoader(new URL[]{});

        Thread.currentThread().setContextClassLoader(urlClassLoaderA);
        // 创建一个指定并行级别的ForkJoinPool
        ForkJoinPool customPool = new ForkJoinPool(4);
        // 使用ForkJoinPool作为parallelStream的执行池
        customPool.submit(new Runnable() {
            @Override
            public void run() {
                Arrays.asList(0, 1, 2, 3, 4).parallelStream().forEach(item -> {
                    System.out.println("name:" + Thread.currentThread().getName() + " tccl:" + Thread.currentThread().getContextClassLoader() + " : " + item);
                });
            }
        }).join(); // 等待任务完成
        customPool.shutdown();

        Thread.currentThread().setContextClassLoader(urlClassLoaderB);
        // 使用ForkJoinPool作为parallelStream的执行池
        new ForkJoinPool(4).submit(new Runnable() {
            @Override
            public void run() {
                Arrays.asList(0, 1, 2, 3, 4).parallelStream().forEach(item -> {
                    System.out.println("name:" + Thread.currentThread().getName() + " tccl:" + Thread.currentThread().getContextClassLoader() + " : " + item);
                });
            }
        }).join(); // 等待任务完成


        Thread.currentThread().setContextClassLoader(old);
        Arrays.asList(5, 6, 7, 8, 9, 10).parallelStream().forEach(item -> {
            System.out.println("name:" + Thread.currentThread().getName() + " tccl:" + Thread.currentThread().getContextClassLoader() + " : " + item);
        });
    }
}
