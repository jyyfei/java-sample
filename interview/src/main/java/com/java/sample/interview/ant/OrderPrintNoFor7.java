package com.java.sample.interview.ant;

import java.util.concurrent.Semaphore;

/**
 * 启动三个线程，要求每个线程打印10次a\b\c
 *
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrintNoFor7 {
    private static Semaphore semaphoreA = new Semaphore(1);
    private static Semaphore semaphoreB = new Semaphore(0);
    private static Semaphore semaphoreC = new Semaphore(0);

    private void aa() {
        System.out.println("aa");
    }

    private void bb() {
        System.out.println("bb");
    }

    private void cc() {
        System.out.println("cc");
    }

    public static void main(String[] args) {
        OrderPrintNoFor7 print = new OrderPrintNoFor7();
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                try {
                    semaphoreC.acquire();
                    print.cc();
                    i++;
                    semaphoreA.release();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, "threadC");
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                try {
                    semaphoreB.acquire();
                    print.bb();
                    i++;
                    semaphoreC.release();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                try {
                    semaphoreA.acquire();
                    System.out.println("===============================" + i + "=================================");
                    print.aa();
                    i++;
                    semaphoreB.release();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, "threadA");

        threadB.start();
        threadC.start();
        threadA.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
