package com.java.sample.async;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class AsyncInitMethodManager
        implements PriorityOrdered, ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    public static final String ASYNC_INIT_METHOD_EXECUTOR_BEAN_NAME = "async-init-method-executor";

    public static final String ASYNC_INIT_METHOD_NAME = "async-init-method-name";

    private final AtomicReference<ExecutorService> executorServiceRef = new AtomicReference<>();

    private final Map<BeanFactory, Map<String, String>> asyncInitBeanNameMap = new ConcurrentHashMap<>();

    private final List<Future<?>> futures = new ArrayList<>();

    private ApplicationContext applicationContext;

    private boolean startUpFinish = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (applicationContext.equals(event.getApplicationContext())) {
            ensureAsyncTasksFinish();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void submitTask(Runnable runnable) {
        if (executorServiceRef.get() == null) {
            ExecutorService executorService = createAsyncExecutorService();
            boolean success = executorServiceRef.compareAndSet(null, executorService);
            if (!success) {
                executorService.shutdown();
            }
        }
        Future<?> future = executorServiceRef.get().submit(runnable);
        futures.add(future);
    }

    private ExecutorService createAsyncExecutorService() {
        return (ExecutorService) applicationContext.getBean(ASYNC_INIT_METHOD_EXECUTOR_BEAN_NAME,
                Supplier.class).get();
    }

    void ensureAsyncTasksFinish() {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Throwable e) {
                throw new RuntimeException("Async init task finish fail", e);
            }
        }

        startUpFinish = true;
        futures.clear();
        asyncInitBeanNameMap.clear();
        if (executorServiceRef.get() != null) {
            executorServiceRef.get().shutdown();
            executorServiceRef.set(null);
        }
    }

    public boolean isStartUpFinish() {
        return startUpFinish;
    }

    public void registerAsyncInitBean(ConfigurableListableBeanFactory beanFactory, String beanName, String asyncInitMethodName) {
        Map<String, String> map = asyncInitBeanNameMap.computeIfAbsent(beanFactory, k -> new ConcurrentHashMap<>());
        map.put(beanName, asyncInitMethodName);
    }

    public String findAsyncInitMethod(ConfigurableListableBeanFactory beanFactory, String beanName) {
        Map<String, String> map = asyncInitBeanNameMap.get(beanFactory);
        if (map == null) {
            return null;
        } else {
            return map.get(beanName);
        }
    }
}
