package com.java.sample.interview.ant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrint3 {

    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    Condition condition2 = lock.newCondition();

    private void aa() {
        lock.lock();
        System.out.println("aa");
        condition.signal();
        lock.unlock();
    }

    private void bb() {
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("bb");
        condition2.signal();
        lock.unlock();
    }

    private void cc() {
        lock.lock();
        try {
            condition2.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("cc");
        lock.unlock();
    }

    public static void main(String[] args) {
        OrderPrint3 orderPrint2 = new OrderPrint3();
        for (int i = 0; i < 10; i++) {
            System.out.println("************************************");
            try {
                Thread threadC = new Thread(() -> orderPrint2.cc(), "threadC");
                Thread threadB = new Thread(() -> orderPrint2.bb(), "threadB");
                Thread threadA = new Thread(() -> orderPrint2.aa(), "threadA");
                threadB.start();
                threadC.start();
                threadA.start();

                threadB.join();
                threadC.join();
                threadA.join();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
