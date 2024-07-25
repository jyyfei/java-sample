package com.spring.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class TestAspect {

    @Pointcut("execution(* com.spring.demo.aop.TestUtil..*(..))")
    public void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object aroundMqMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj;
        System.out.println("cutcutcutcutcutcutcutcut");
        obj = joinPoint.proceed();
        return obj;
    }

}