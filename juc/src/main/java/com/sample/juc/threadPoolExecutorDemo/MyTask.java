package com.sample.juc.threadPoolExecutorDemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 线程类
 */
class MyTask implements Runnable {
    private int num;

    public MyTask(int num) {
        this.num = num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task " + num);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task " + num + "执行完毕");
    }

    /**
     * 获取（未来时间戳-当前时间戳）的差值，
     * 也即是：（每个线程的睡醒时间戳-每个线程的入睡时间戳）
     * 作用：用于实现多线程高并发
     *
     * @return
     * @throws ParseException
     */
    public long getDelta() throws ParseException {
        //获取当前时间戳
        long t1 = System.currentTimeMillis();
        //获取未来某个时间戳（自定义，可写入配置文件）
        String str = "2016-11-11 15:15:15";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long t2 = simpleDateFormat.parse(str).getTime();
        return t2 - t1;
    }
}