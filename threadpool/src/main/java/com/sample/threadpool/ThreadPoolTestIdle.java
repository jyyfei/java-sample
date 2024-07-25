package com.sample.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 测试线程池非code线程超过keepAlive时间后销毁的原理
 * 1、从阻塞队列获取任务的使用keepAlive作为获取超时时间workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS)
 * 2、当获取不到结果时线程执行内部变量的timeout被置为true，当worker的数量大于核心线程数时time是true，java.util.concurrent.ThreadPoolExecutor#getTask()会return null
 * 3、java.util.concurrent.ThreadPoolExecutor#runWorker(java.util.concurrent.ThreadPoolExecutor.Worker)中while条件不满足，runWorker执行结束，线程随之结束销毁，从workers中删除自身worker
 * 线程池是如何保证code线程不被销毁的呢？
 * allowCoreThreadTimeOut默认false
 * 1、当allowCoreThreadTimeOut为true时即使线程数小于core也是会销毁线程
 * 2、当allowCoreThreadTimeOut为false时，在ThreadPoolExecutor#getTask()中会判断线程数量大于code时才会去销毁线程
 * 会不会有并发问题导致多个线程同时判断线程数大于code去触发销毁呢？
 * 不会，因为线程数量减少时cas操作，同时只有一个线程会compareAndDecrementWorkerCount成功并return null触发销毁
 *
 * @author yunfei.jyf
 */
public class ThreadPoolTestIdle {
    public static void main(String[] args) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                0, 1,
                10L, TimeUnit.SECONDS,
                queue);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.println(queue.size());

        try {
            Thread.sleep(100000000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
