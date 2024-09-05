package com.java.sample.interview.ant;

import java.util.concurrent.CyclicBarrier;

/**
 * 启动三个线程，要求每个线程打印10次a\b\c
 *
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrintNoFor5 {
    private static CyclicBarrier cyclicBarrier1 = new CyclicBarrier(3);
    private static CyclicBarrier cyclicBarrier2 = new CyclicBarrier(3);
    private static CyclicBarrier cyclicBarrier3 = new CyclicBarrier(3);

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
        OrderPrintNoFor5 print = new OrderPrintNoFor5();
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                try {
                    cyclicBarrier1.await();
                    cyclicBarrier2.await();
                    print.cc();
                    i++;
                    cyclicBarrier3.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, "threadC");
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                try {
                    cyclicBarrier1.await();
                    print.bb();
                    i++;
                    cyclicBarrier2.await();
                    cyclicBarrier3.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, "threadB");
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                try {
                    System.out.println("===============================" + i + "=================================");
                    print.aa();
                    i++;
                    cyclicBarrier1.await();
                    cyclicBarrier2.await();
                    cyclicBarrier3.await();
                } catch (Exception e) {

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
        System.out.println("Parties" + cyclicBarrier1.getParties());
        System.out.println("NumberWaiting:" + cyclicBarrier1.getNumberWaiting());
    }
}
