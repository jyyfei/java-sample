package com.java.sample.async;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class AsyncInitializeBeanMethodInvoker implements MethodInterceptor {

    private final AsyncInitMethodManager asyncInitMethodManager;

    private final Object targetObject;

    private final String asyncMethodName;

    private final String beanName;

    private final CountDownLatch initCountDownLatch = new CountDownLatch(1);

    /**
     * mark async-init method is during first invocation.
     */
    private volatile boolean isAsyncCalling = false;

    /**
     * mark init-method is called.
     */
    private volatile boolean isAsyncCalled = false;

    public AsyncInitializeBeanMethodInvoker(AsyncInitMethodManager asyncInitMethodManager,
                                            Object targetObject, String beanName, String methodName) {
        this.asyncInitMethodManager = asyncInitMethodManager;
        this.targetObject = targetObject;
        this.beanName = beanName;
        this.asyncMethodName = methodName;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        // if the spring refreshing is finished
        if (asyncInitMethodManager.isStartUpFinish()) {
            return invocation.getMethod().invoke(targetObject, invocation.getArguments());
        }

        Method method = invocation.getMethod();
        final String methodName = method.getName();
        if (!isAsyncCalled && methodName.equals(asyncMethodName)) {
            isAsyncCalled = true;
            isAsyncCalling = true;
            asyncInitMethodManager.submitTask(new AsyncBeanInitRunnable(invocation));
            return null;
        }

        if (isAsyncCalling) {
            long startTime = System.currentTimeMillis();
            initCountDownLatch.await();
            log.info("{}({}) {} method wait {}ms.", targetObject.getClass().getName(), beanName,
                    methodName, (System.currentTimeMillis() - startTime));
        }
        return invocation.getMethod().invoke(targetObject, invocation.getArguments());
    }

    void asyncMethodFinish() {
        this.initCountDownLatch.countDown();
        this.isAsyncCalling = false;
    }

    private class AsyncBeanInitRunnable implements Runnable {

        private final MethodInvocation invocation;

        public AsyncBeanInitRunnable(MethodInvocation invocation) {
            this.invocation = invocation;
        }

        @Override
        public void run() {
            try {
                long startTime = System.currentTimeMillis();
                invocation.getMethod().invoke(targetObject, invocation.getArguments());
                log.info("{}({}) {} method execute {}dms.", targetObject.getClass().getName(),
                        beanName, invocation.getMethod().getName(),
                        (System.currentTimeMillis() - startTime));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            } finally {
                AsyncInitializeBeanMethodInvoker.this.asyncMethodFinish();
            }
        }
    }
}
