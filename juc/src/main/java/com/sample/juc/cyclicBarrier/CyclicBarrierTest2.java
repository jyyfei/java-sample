package com.sample.juc.cyclicBarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * @author yunfei.jyf
 * @date 2024/9/5
 */
public class CyclicBarrierTest2 {

    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    System.out.println("thread interrupted:" + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        thread.interrupt();

        try {
            cyclicBarrier.await();
        } catch (Exception e) {
            System.out.println("main interrupted:" + e.getMessage());
            System.out.println("main isBroken:" + cyclicBarrier.isBroken());
            throw new RuntimeException(e);
        }
    }
}
