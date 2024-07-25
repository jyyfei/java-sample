package com.sample.juc.lock;

public class NonReentryLockTest {
    static NonReentryLock nonReentryLock = new NonReentryLock();
    public static void print() throws InterruptedException {
        nonReentryLock.lock();
        System.out.println("11111111");
        doAdd();
        nonReentryLock.unlock();
    }
    public static void doAdd() throws InterruptedException {
        nonReentryLock.lock();
        System.out.println("22222222");
        nonReentryLock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        print();
    }
}