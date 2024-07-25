package com.sample.juc.threadPoolExecutorDemo;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author JiYunfei
 * @date 18-8-7
 */
public class TestScheduleExecutorService {
    public static void main(String[] args) throws Exception {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
        Runnable runnable = () -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("run....");
        };
//        scheduledThreadPoolExecutor.schedule(runnable, 1, TimeUnit.SECONDS);// 1s后执行一次

//        ScheduledFuture<String> schedule = scheduledThreadPoolExecutor.schedule(() -> {
//            Thread.sleep(10000);
//            return "nihao";
//        }, 1, TimeUnit.SECONDS);
//        System.out.println(new Date());
//        System.out.println(schedule.get());
//        System.out.println(new Date());

//        scheduledThreadPoolExecutor.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);// 1s后执行，每次开始时间相差1s

//        scheduledThreadPoolExecutor.scheduleWithFixedDelay(runnable, 1, 1, TimeUnit.SECONDS);// 第二次开始时间与第一次结束时间相差1s

    }
}
