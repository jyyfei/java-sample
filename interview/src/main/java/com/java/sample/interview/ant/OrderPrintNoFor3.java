package com.java.sample.interview.ant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 启动三个线程，要求每个线程打印10次a\b\c
 * 按顺序输出
 * 思路：
 *
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrintNoFor3 {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();
    private static Condition condition3 = lock.newCondition();

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
        OrderPrintNoFor3 print = new OrderPrintNoFor3();
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                lock.lock();
                try {
                    condition3.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                print.cc();
                i++;
                condition1.signal();
                lock.unlock();
            }
        }, "threadC");
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                lock.lock();
                try {
                    condition2.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                print.bb();
                i++;
                condition3.signal();
                lock.unlock();
            }
        }, "threadB");
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                lock.lock();
                System.out.println("******************" + i + "******************");
                print.aa();
                i++;
                condition2.signal();
                try {
                    condition1.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lock.unlock();
            }
        }, "threadA");

        threadB.start();
        threadC.start();
        threadA.start();
    }
}
