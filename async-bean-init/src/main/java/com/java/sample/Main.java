package com.java.sample;

import com.java.sample.bean.TestBeanAA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author yunfei.jyf
 * @date 2024/7/25
 */
@SpringBootApplication
public class Main {
    @Bean(initMethod = "init")
    public TestBeanAA testBeanAA() {
        try {
            return new TestBeanAA();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean(initMethod = "init")
    public TestBeanAA testBeanAA22() {
        try {
            return new TestBeanAA();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}