package com.sample.juc.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ReentryLockTest {
    static ReentryLock lock = new ReentryLock();

    public static void print(int i) throws InterruptedException {
        lock.lock();
        System.out.println(i);
        doAdd(i);
        lock.unlock();
    }

    public static void doAdd(int i) throws InterruptedException {
        lock.lock();
        System.out.println("add" + i);
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(2, 3, 1,
                TimeUnit.SECONDS, new SynchronousQueue<>(), r -> new Thread(r, "jyfThread"),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executorService.execute(() -> {
            try {
                print(1);
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() -> {
            try {
                print(2);
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() -> {
            try {
                print(3);
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() -> {
            try {
                print(4);
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}