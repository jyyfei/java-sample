package com.java.sample.interview.ant;

/**
 * 启动三个线程，要求每个线程打印10次a\b\c
 * 按顺序输出，注意java.lang.Object#notify()后及时释放锁，不然一直占有锁，notify没有用
 *
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrintNoFor4 {
    private static Object lock1 = new Object();
    private static Object lock2 = new Object();
    private static Object lock3 = new Object();

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
        OrderPrintNoFor4 print = new OrderPrintNoFor4();
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                synchronized (lock3) {
                    try {
                        lock3.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    print.cc();
                    i++;
                    synchronized (lock1) {
                        lock1.notify();
                    }
                }
            }
        }, "threadC");
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                synchronized (lock2) {
                    try {
                        lock2.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    print.bb();
                    i++;
                    synchronized (lock3) {
                        lock3.notify();
                    }
                }
            }
        }, "threadB");
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                synchronized (lock1) {
                    System.out.println("******************" + i + "******************");
                    print.aa();
                    i++;
                    synchronized (lock2) {
                        lock2.notify();
                    }
                    try {
                        lock1.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }, "threadA");

        threadB.start();
        threadC.start();
        threadA.start();
    }
}
