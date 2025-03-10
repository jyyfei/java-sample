package com.spring.demo.circularReference;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yunfei.jyf
 * @date 2024/11/4
 */
@Component
public class ServiceB {
    @Lazy
    @Resource
    private ServiceA serviceA;

    @Async("galaxy_care_common")
    public void testB() {

    }
}
