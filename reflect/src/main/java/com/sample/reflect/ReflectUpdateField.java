package com.sample.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yunfei.jyf
 * @date 2024/5/16
 */
public class ReflectUpdateField {
    public static void main2(String[] args) throws Exception {
        System.out.println(HttpClientBeanHolder.get());
        Field alreadyShutdown = HttpClientBeanHolder.class.getDeclaredField("ALREADY_SHUTDOWN");
        alreadyShutdown.setAccessible(true);
        Object object = alreadyShutdown.get(null);

        Method compareAndSetMethod = AtomicBoolean.class.getMethod("compareAndSet", boolean.class, boolean.class);
        compareAndSetMethod.invoke(object, false, true);
        System.out.println(HttpClientBeanHolder.get());
    }

    public static void main(String[] args) throws Exception {
        System.out.println(HttpClientBeanHolder.get());
        Method shutdownMethod = HttpClientBeanHolder.class.getDeclaredMethod("shutdown");
        shutdownMethod.setAccessible(true);
        shutdownMethod.invoke(null);
        System.out.println(HttpClientBeanHolder.get());
    }
}
