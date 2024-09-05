package com.sample.juc.cyclicBarrier;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yunfei.jyf
 * @date 2024/9/5
 */
public class MyCyclicBarrier {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final int parties;
    private int count;
    private Runnable runnable;

    public MyCyclicBarrier(int parties) {
        this(parties, null);
    }

    public MyCyclicBarrier(int parties, Runnable runnable) {
        this.parties = parties;
        this.count = this.parties;
        this.runnable = runnable;
    }

    public void await() {
        lock.lock();
        try {
            count = count - 1;
            if (count == 0) {
                if (runnable != null) {
                    runnable.run();
                }
                condition.signalAll();
                count = parties;
            } else {
                condition.await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
