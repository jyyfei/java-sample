package com.sample.juc.interrupt;

/**
 * @author yunfei.jyf
 * @date 2024/9/10
 */
public class InterruptSample {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("22");
                    }
                    if (true) {
                        System.out.println("11");
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException");
                    }
                }
            }
        });
        thread.start();
        thread.interrupt();
        System.out.println(thread.isInterrupted());
    }
}
