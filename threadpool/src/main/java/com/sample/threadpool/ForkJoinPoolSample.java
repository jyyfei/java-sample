package com.sample.threadpool;

import java.util.concurrent.ForkJoinPool;

/**
 * 工作窃取算法
 * 此算法允许空闲的线程从其他正忙的线程那里窃取任务来执行，从而最大化CPU利用率和提高整体的处理速度
 * 原理？
 * 工作窃取算法的核心思想是维护一个双端队列（deque），每个工作线程都有自己的一个队列，
 * 用于存放分配给这个线程的任务。工作线程使用自己队列的底端去添加任务或移除任务，
 * 当一个线程完成了自己队列中的所有任务时，它可以从其他线程的队列的顶端窃取任务来执行
 *
 * @author yunfei.jyf
 * @date 2024/6/6
 */
public class ForkJoinPoolSample {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.println(forkJoinPool.getQueuedTaskCount());
        forkJoinPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.println(forkJoinPool.getQueuedTaskCount());
    }
}
