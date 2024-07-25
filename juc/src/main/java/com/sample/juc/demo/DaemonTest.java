package com.sample.juc.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 验证守护线程的特性
 * 当进程中没有非demo的线程时，守护线程退出，进程退出
 */
public class DaemonTest {
    public static void main2(String[] args) {
        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                boolean stop = false;
                while (!stop) {
                    System.out.println(1);
                    Thread.sleep(3000);
                }
                return "123";
            }
        };

        FutureTask<String> futureTask = new FutureTask<String>(callable);

        Thread thread = new Thread(futureTask);
        thread.setDaemon(true);
        thread.start();

        System.out.println("thread1 start");

        Callable<String> callable2 = new Callable<String>() {
            public String call() throws Exception {
                boolean stop = false;
                while (!stop) {
                    System.out.println(2);
                    Thread.sleep(3000);
                }
                return "123";
            }
        };

        FutureTask<String> futureTask2 = new FutureTask<String>(callable2);
        Thread thread2 = new Thread(futureTask);
        thread2.setDaemon(false);
        thread2.start();
        System.out.println("thread2 start");

        System.out.println("main end");
    }

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            public void run() {
                boolean stop = false;
                while (!stop) {
                    System.out.println(1);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();

        System.out.println("thread1 start");

        Thread thread2 = new Thread(runnable);
        thread2.setDaemon(false);
        thread2.start();
        System.out.println("thread2 start");

        System.out.println("main end");
    }
}
