package com.sample.juc.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @author yunfei.jyf
 * @date 2024/9/5
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        // 信号量测试
        Semaphore semaphoreA = new Semaphore(1);

        Thread threadAA = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphoreA.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("AA");
                System.out.println("AA");
                System.out.println("AA");
                System.out.println("AA");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("AA");
                System.out.println("AA");
                System.out.println("AA");
                semaphoreA.release();
            }
        });

        Thread threadBB = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    semaphoreA.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("BB");
                System.out.println("BB");
                System.out.println("BB");
                System.out.println("BB");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("BB");
                System.out.println("BB");
                System.out.println("BB");
                semaphoreA.release();
            }
        });

        threadAA.start();
        threadBB.start();
    }
}
