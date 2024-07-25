package com.sample.reflect;

import java.util.concurrent.atomic.AtomicBoolean;

public final class HttpClientBeanHolder {
    private static final AtomicBoolean ALREADY_SHUTDOWN = new AtomicBoolean(false);

    public static AtomicBoolean get() {
        return ALREADY_SHUTDOWN;
    }

    private static void shutdown() {
        if (ALREADY_SHUTDOWN.compareAndSet(false, true)) {
        }
    }
}
