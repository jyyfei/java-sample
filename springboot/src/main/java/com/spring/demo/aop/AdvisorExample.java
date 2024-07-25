package com.spring.demo.aop;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

public class AdvisorExample {
    public static void main(String[] args) {
        // 创建一个目标对象
        TargetObject targetObject = new TargetObject();

        // 创建一个切点对象
        Pointcut pointcut = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return "sayHello".equals(method.getName());
            }
        };

        // 创建一个前置通知对象
        MethodBeforeAdvice advice = new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println("Before advice is called.");
            }
        };

        // 创建一个Advisor对象
        AbstractBeanFactoryPointcutAdvisor advisor = new AbstractBeanFactoryPointcutAdvisor() {
            @Override
            public Pointcut getPointcut() {
                return pointcut;
            }
        };
        advisor.setAdvice(advice);

        // 创建一个AOP代理对象
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(targetObject);
        proxyFactory.addAdvisor(advisor);

        // 使用AOP代理对象调用目标方法
        TargetInterface proxy = (TargetInterface) proxyFactory.getProxy();
        proxy.sayHello();
    }

    private interface TargetInterface {
        void sayHello();
    }

    private static class TargetObject implements TargetInterface {
        @Override
        public void sayHello() {
            System.out.println("Hello, World!");
        }
    }
}
