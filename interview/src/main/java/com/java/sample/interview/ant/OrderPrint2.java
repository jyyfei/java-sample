package com.java.sample.interview.ant;

import java.util.concurrent.locks.LockSupport;

/**
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrint2 {
    private void aa(Thread thread) {
        System.out.println("aa");
        LockSupport.unpark(thread);
    }

    private void bb(Thread thread) {
        LockSupport.park();
        System.out.println("bb");
        LockSupport.unpark(thread);
    }

    private void cc() {
        LockSupport.park();
        System.out.println("cc");
    }

    public static void main(String[] args) {
        OrderPrint2 orderPrint2 = new OrderPrint2();
        for (int i = 0; i < 10; i++) {
            System.out.println("************************************");
            try {
                Thread threadC = new Thread(() -> orderPrint2.cc(), "threadC");
                Thread threadB = new Thread(() -> orderPrint2.bb(threadC), "threadB");
                Thread threadA = new Thread(() -> orderPrint2.aa(threadB), "threadA");
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
