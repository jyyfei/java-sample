package com.java.sample.interview.ant;

/**
 * 启动三个线程，要求每个线程打印10次a\b\c
 * 按顺序输出
 *
 * 这是一个典型有问题的场景，最后结果也不会按预期输出
 * 通过jstack查看，
 * threadA是waiting状态，只释放了锁lock1，wait代表释放lock1，还占用锁lock2
 * threadB是BLOCKED状态，在等待锁lock2，wait代表释放lock2，同时还占用锁lock3
 * threadC是BLOCKED状态，在等待锁lock3，没占用任何锁
 *
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrintNoFor4Error {
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
        OrderPrintNoFor4Error print = new OrderPrintNoFor4Error();
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                synchronized (lock3) {
                    synchronized (lock1) {
                        try {
                            lock3.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        print.cc();
                        i++;
                        lock1.notify();
                    }
                }
            }
        }, "threadC");
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                synchronized (lock2) {
                    synchronized (lock3) {
                        try {
                            lock2.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        print.bb();
                        i++;
                        lock3.notify();
                    }
                }
            }
        }, "threadB");
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                synchronized (lock1) {
                    synchronized (lock2) {
                        System.out.println("******************" + i + "******************");
                        print.aa();
                        i++;
                        lock2.notify();
                        try {
                            lock1.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }, "threadA");

        Thread threadD = new Thread(() -> {
            for (int i = 0; i < 10; ) {
                System.out.println("******************D" + i + "D******************");
                i++;
            }
        }, "threadD");

        threadB.start();
        threadC.start();
        threadA.start();
        threadD.start();
    }
}