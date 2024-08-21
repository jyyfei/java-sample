package com.java.sample.interview.ant;

/**
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrint4 {

    Object condition = new Object();
    Object condition2 = new Object();

    private void aa() {
        synchronized (condition) {
            System.out.println("aa");
            condition.notify();
        }
    }

    private void bb() {
        synchronized (condition) {
            try {
                condition.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("bb");
            synchronized (condition2) {
                condition2.notify();
            }
        }
    }

    private void cc() {
        synchronized (condition2) {
            try {
                condition2.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("cc");
        }
    }

    public static void main(String[] args) {
        OrderPrint4 orderPrint2 = new OrderPrint4();
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
