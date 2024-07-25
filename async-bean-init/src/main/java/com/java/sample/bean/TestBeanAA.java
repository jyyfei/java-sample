package com.java.sample.bean;

import com.java.sample.async.AsyncInit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yunfei.jyf
 * @date 2024/7/25
 */
@AsyncInit
@Slf4j
public class TestBeanAA {
    public TestBeanAA() throws Exception {
        log.info("TestBeanAA 构造方法 睡3秒");
        Thread.sleep(3 * 1000);
    }

    public void init() throws Exception {
        log.info("TestBeanAA init 睡10秒, thread:{}", Thread.currentThread().getName());
        Thread.sleep(10 * 1000);
    }
}
