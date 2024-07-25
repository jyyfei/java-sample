package com.sample.juc.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class AQSExample {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(50);

        int[] i = {0};
        int[] name = {0};
        ReentrantLock lock = new ReentrantLock(true);
        for (int i1 = 0; i1 < 50; i1++) {
            Thread thread = new Thread(() -> {
                lock.lock();
                try {
                    i[0]++;
                    System.out.println(Thread.currentThread().getName() + " add success");
                } finally {
                    lock.unlock();
                    countDownLatch.countDown();
                }
            });
            thread.setName("Thread-" + (++name[0]));
            thread.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end:" + i[0]);
    }
}
