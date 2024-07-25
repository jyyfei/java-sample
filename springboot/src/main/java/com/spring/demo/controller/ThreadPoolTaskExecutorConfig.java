package com.spring.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 创建自定义的线程池
 */
@Configuration
public class ThreadPoolTaskExecutorConfig {

    public static final String ASYNC_TASK_BEAN_NAME = "asyncTask";

    @Value("${demo.async.worker-num:200}")
    private Integer asyncWorkerNum;
    @Value("${demo.async.task-size:10240}")
    private Integer asyncTaskSize;

    @Bean(ASYNC_TASK_BEAN_NAME)
    public ThreadPoolTaskExecutor asyncTask() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(asyncWorkerNum);
        // 设置最大线程数
        executor.setMaxPoolSize(asyncWorkerNum);
        // 设置队列容量
        executor.setQueueCapacity(asyncTaskSize);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("Async-Executor-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

}