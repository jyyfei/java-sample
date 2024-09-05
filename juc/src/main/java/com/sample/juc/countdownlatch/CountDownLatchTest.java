package com.sample.juc.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * @author yunfei.jyf
 * @date 2024/9/5
 */
public class CountDownLatchTest {
    private static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("bb");
                countDownLatch.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("aa");
                countDownLatch.countDown();
            }
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("cc");
    }
}
