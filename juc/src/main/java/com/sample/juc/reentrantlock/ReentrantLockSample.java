package com.sample.juc.reentrantlock;

import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yunfei.jyf
 * @date 2024/9/10
 */
public class ReentrantLockSample {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    System.out.println("11");
                    lock.lock();
                    System.out.println("22");
                    boolean tryLock = lock.tryLock();
                    System.out.println(tryLock);
                } finally {
                    lock.unlock();
                }
                System.out.println(lock.isLocked());
                lock.unlock();
                System.out.println(lock.isLocked());

                try {
                    Method getOwnerMethod = ReentrantLock.class.getDeclaredMethod("getOwner");
                    getOwnerMethod.setAccessible(true);
                    Thread ownerThread = (Thread) getOwnerMethod.invoke(lock);
                    System.out.println("Lock owner: " + ownerThread.getName());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                lock.unlock();
                System.out.println(lock.isLocked());

                try {
                    lock.lockInterruptibly();
                    System.out.println("qqq");
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException");
                    System.out.println(lock.isLocked());
                } finally {
                    lock.unlock();
                }
            }
        });
        thread.setName("test");
        thread.start();
        thread.interrupt();
    }
}
