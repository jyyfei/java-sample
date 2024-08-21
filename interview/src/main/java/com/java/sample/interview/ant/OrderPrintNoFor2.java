package com.java.sample.interview.ant;

/**
 * 启动三个线程，要求每个线程打印10次a\b\c
 * 按顺序输出
 * 思路：不用锁，三个线程一直跑，本质上线程间还是乱序的，只是通过volatile变量控制是否打印，以此实现顺序打印
 *
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrintNoFor2 {
    private static volatile int state = 0;

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
        OrderPrintNoFor2 print = new OrderPrintNoFor2();
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                if (state % 3 == 2) {
                    print.cc();
                    i++;
                    state++;
                }
            }
        }, "threadC");
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                if (state % 3 == 1) {
                    print.bb();
                    i++;
                    state++;
                }
            }
        }, "threadB");
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                if (state % 3 == 0) {
                    System.out.println("******************" + i + "******************");
                    print.aa();
                    i++;
                    state++;
                }
            }
        }, "threadA");

        threadB.start();
        threadC.start();
        threadA.start();
    }
}
