package com.java.sample.interview.ant;

/**
 * 启动三个线程，要求每个线程打印10次a\b\c
 * 按顺序输出
 * 思路：三个线程启动后，开始竞争同一把锁，竞争通过的判断是否可以执行语句打印，以此异步转同步
 * 缺点：存在无效竞争
 * 优化：
 *
 * @author yunfei.jyf
 * @date 2024/8/19
 */
public class OrderPrintNoFor1 {
    private static int state = 0;
    private static final Object lock = new Object();

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
        OrderPrintNoFor1 print = new OrderPrintNoFor1();
        Thread threadC = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                synchronized (lock) {
                    if (state % 3 == 2) {
                        print.cc();
                        i++;
                        state++;
                    }
                }
            }
        }, "threadC");
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                synchronized (lock) {
                    if (state % 3 == 1) {
                        print.bb();
                        i++;
                        state++;
                    }
                }
            }
        }, "threadB");
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 100; ) {
                synchronized (lock) {
                    if (state % 3 == 0) {
                        System.out.println("******************" + i + "******************");
                        print.aa();
                        i++;
                        state++;
                    }
                }
            }
        }, "threadA");

        threadB.start();
        threadC.start();
        threadA.start();
    }
}
