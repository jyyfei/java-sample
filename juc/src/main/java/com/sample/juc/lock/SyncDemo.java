package com.sample.juc.lock;

/**
 * 测试syn锁的不可中断性
 */
public class SyncDemo {
    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (SyncDemo.class) {
                    try {
                        Thread.sleep(1000 * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(1);
                }
            }
        });
        thread1.setName("test1");
        thread1.start();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(1000 * 10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(2);
                synchronized (SyncDemo.class) {
                    System.out.println(2);
                }
            }
        });
        thread.setName("test");
        thread.start();
        try {
            Thread.sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();//已经中断了，thread还会执行，所以synchronized具有不可中断性
        System.out.println("stop");

    }
}
