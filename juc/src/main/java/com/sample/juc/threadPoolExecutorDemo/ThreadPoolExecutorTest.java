package com.sample.juc.threadPoolExecutorDemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用ThreadPoolExecutor类创建线程池
 * 一、采用这种方式的优点：
 * 可以实时获取线程池内线程的各种状态
 * 可以动态调整线程池大小
 * 二、线程池的工作原理简介：
 * 如果当前线程池中的线程数目小于corePoolSize，则每来一个任务，就会创建一个线程去执行这个任务；
 * 如果当前线程池中的线程数目>=corePoolSize，则每来一个任务，会尝试将其添加到任务缓存队列当中，若添加成功，则该任务会等待空闲线程将其取出去执行；若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程去执行这个任务；
 * 如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；
 * 如果线程池中的线程数量大于corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。
 * <p>
 * <p>
 * <p>
 * ThreadPoolExecutor类的使用方法
 * 实现高并发：在线程类中的run（）方法内设置Thread.sleep（long delta）； delta取值为：（并发开始时间戳 - 线程开始时间戳）
 * Created by Administrator on 2016/11/19.
 */
public class ThreadPoolExecutorTest {

    //本例子最多30个线程在执行，5个在队列，处理35个请求，再多会执行默认的拒绝策略。ThreadPoolExecutor中的默认拒绝策略为defaultHandler = new AbortPolicy();
    public static void main(String[] args) {

        //设置核心池大小
        int corePoolSize = 5;
        int maximumPoolSize = 30;

        //设置线程池最大能接受多少线程

        //当前线程数大于corePoolSize、小于maximumPoolSize时，超出corePoolSize的线程数的生命周期
        long keepActiveTime = 200;

        //设置时间单位，秒
        TimeUnit timeUnit = TimeUnit.SECONDS;

        //设置线程池缓存队列的排队策略为FIFO，并且指定缓存队列大小为5
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);

        //创建ThreadPoolExecutor线程池对象，并初始化该对象的各种参数
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepActiveTime, timeUnit, workQueue);

        //往线程池中循环提交线程
        for (int i = 0; i < 40; i++) {
            //创建线程类对象
            MyTask myTask = new MyTask(i);
            //开启线程
            executor.execute(myTask);
            //获取线程池中线程的相应参数
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" + executor.getQueue().size() + "，已执行完的任务数目：" + executor.getCompletedTaskCount());
        }
        //待线程池以及缓存队列中所有的线程任务完成后关闭线程池。
        executor.shutdown();
    }
}