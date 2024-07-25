package com.sample.juc.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitNotifyExample {
    private Object lock = new Object();

    public void before() {
        synchronized (WaitNotifyExample.class) {
            System.out.println("before");
            WaitNotifyExample.class.notifyAll();
        }
    }
    public void after() {
        synchronized (WaitNotifyExample.class) {
            try {
                WaitNotifyExample.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("after");
        }
    }
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        WaitNotifyExample example = new WaitNotifyExample();
        executorService.execute(() -> example.after());
        executorService.execute(() -> example.before());

        executorService.shutdown();
    }
}