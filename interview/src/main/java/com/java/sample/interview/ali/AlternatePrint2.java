package com.java.sample.interview.ali;

/**
 * 多线程：
 * 如下程序通过2个线程顺序循环打印从0至100，A线程打印奇数，B现场打印偶数，并且保障顺序输出：
 * threadA: 1
 * threadB: 2
 * threadA: 3
 * threadB: 4
 * threadA: 5
 * ...
 */
public class AlternatePrint2 {

    private volatile int i = 1;

    public static void main(String[] args) {
        new AlternatePrint2().alternateRun();
    }

    /**
     * 两线程交替执行，打印自己要打印的数字
     */
    private void alternateRun() {
        Thread threadA = new Thread(() -> {
            while (i <= 10000) {
                if (!isEven(i)) {
                    System.out.println("threadA: " + i);
                    i++;
                }
            }
        });
        threadA.start();

        Thread threadB = new Thread(() -> {
            while (i <= 10000) {
                if (isEven(i)) {
                    System.out.println("threadB: " + i + "\n----------------------");
                    i++;
                }
            }
        });
        threadB.start();
    }

    /**
     * 判断i是否为偶数
     *
     * @param i
     * @return
     */
    private boolean isEven(int i) {
        if (i % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
