package com.sample.classloader.whenLoad;

/**
 * @author yunfei.jyf
 * @date 2024/5/7
 */
public class TestAA {
    private static final ThreadLocal<TestBB> CLIENT_RESPONSE_LOCAL = new ThreadLocal<TestBB>() {
        protected TestBB initialValue() {
            return new TestBB();
        }
    };

    public static TestBB get() {
        return new TestBB();
    }

    public static TestAA getInstance() {
        return LazyInstanceHolder.INSTANCE;
    }

    private final static class LazyInstanceHolder {
        private final static TestAA INSTANCE = new TestAA();
    }
}
