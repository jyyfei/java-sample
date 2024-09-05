package com.java.sample.interview.ant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 启动三个线程，要求每个线程打印10次a\b\c
 *
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrintNoFor6 {
    private static MyCyclicBarrier cyclicBarrier1 = new MyCyclicBarrier(3);
    private static MyCyclicBarrier cyclicBarrier2 = new MyCyclicBarrier(3);
    private static MyCyclicBarrier cyclicBarrier3 = new MyCyclicBarrier(3);

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
        OrderPrintNoFor6 print = new OrderPrintNoFor6();
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
    }

    private static class MyCyclicBarrier {
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();
        private final int parties;
        private int count;
        private Runnable runnable;

        public MyCyclicBarrier(int parties) {
            this(parties, null);
        }

        public MyCyclicBarrier(int parties, Runnable runnable) {
            this.parties = parties;
            this.count = this.parties;
            this.runnable = runnable;
        }

        public void await() {
            lock.lock();
            try {
                count = count - 1;
                if (count == 0) {
                    if (runnable != null) {
                        runnable.run();
                    }
                    condition.signalAll();
                    count = parties;
                } else {
                    condition.await();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
}
