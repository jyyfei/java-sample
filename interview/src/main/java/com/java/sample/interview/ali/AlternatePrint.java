package com.java.sample.interview.ali;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交替打印
 */
public class AlternatePrint {

    private volatile int i = 1;

    public static void main(String[] args) {
        new AlternatePrint().alternateRun();
    }

    /**
     * 两线程交替执行，打印自己要打印的数字
     */
    private void alternateRun() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();


        Thread threadA = new Thread(() -> {
            while (i <= 100) {
                if (!isEven(i)) {
                    lock.lock();

                    System.out.println("threadA: " + i++);
                    condition.signal();
                    try {
                        if (i <=  100) {
                            condition.await();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    lock.unlock();
                }
            }
        });
        threadA.setName("ThreadA");
        threadA.start();

        Thread threadB = new Thread(() -> {
            while (i <= 100) {
                if (isEven(i)) {
                    lock.lock();

                    System.out.println("threadB: " + i++);
                    condition.signal();
                    try {
                        if (i <=  100) {
                            condition.await();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    lock.unlock();
                }
            }
        });
        threadB.setName("ThreadB");
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
