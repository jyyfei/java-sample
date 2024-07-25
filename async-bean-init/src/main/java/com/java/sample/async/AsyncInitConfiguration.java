package com.java.sample.async;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class AsyncInitConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AsyncInitMethodManager asyncInitMethodManager() {
        return new AsyncInitMethodManager();
    }

    @Bean(AsyncInitMethodManager.ASYNC_INIT_METHOD_EXECUTOR_BEAN_NAME)
    @ConditionalOnMissingBean(name = AsyncInitMethodManager.ASYNC_INIT_METHOD_EXECUTOR_BEAN_NAME)
    public Supplier<ExecutorService> asyncInitMethodExecutor() {
        return () -> {
            int coreSize = 8;
            int maxSize = 8;
            Assert.isTrue(coreSize >= 0, "executorCoreSize must no less than zero");
            Assert.isTrue(maxSize >= 0, "executorMaxSize must no less than zero");

            log.info("create async-init-bean thread pool, corePoolSize: {}, maxPoolSize: {}.",
                    coreSize, maxSize);
            return new ThreadPoolExecutor(coreSize, maxSize, 30, TimeUnit.SECONDS,
                    new SynchronousQueue<>(), new NamedThreadFactory("async-init-bean", false),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public AsyncProxyBeanPostProcessor asyncProxyBeanPostProcessor(
            AsyncInitMethodManager asyncInitMethodManager) {
        return new AsyncProxyBeanPostProcessor(asyncInitMethodManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public AsyncInitBeanFactoryPostProcessor asyncInitBeanFactoryPostProcessor() {
        return new AsyncInitBeanFactoryPostProcessor();
    }
}