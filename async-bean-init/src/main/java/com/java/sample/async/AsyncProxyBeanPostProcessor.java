package com.java.sample.async;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.StringUtils;

import static com.java.sample.async.AsyncInitMethodManager.ASYNC_INIT_METHOD_NAME;

public class AsyncProxyBeanPostProcessor
        implements BeanPostProcessor, InitializingBean, BeanFactoryAware, Ordered {

    private final AsyncInitMethodManager asyncInitMethodManager;

    private ConfigurableListableBeanFactory beanFactory;

    public AsyncProxyBeanPostProcessor(AsyncInitMethodManager asyncInitMethodManager) {
        this.asyncInitMethodManager = asyncInitMethodManager;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        String methodName = asyncInitMethodManager.findAsyncInitMethod(beanFactory, beanName);
        if (!StringUtils.hasText(methodName)) {
            return bean;
        }

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTargetClass(bean.getClass());
        proxyFactory.setProxyTargetClass(true);
        AsyncInitializeBeanMethodInvoker asyncInitializeBeanMethodInvoker = new AsyncInitializeBeanMethodInvoker(
                asyncInitMethodManager, bean, beanName, methodName);
        proxyFactory.addAdvice(asyncInitializeBeanMethodInvoker);
        return proxyFactory.getProxy();
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String asyncInitMethodName = (String) beanDefinition
                    .getAttribute(ASYNC_INIT_METHOD_NAME);
            if (StringUtils.hasText(asyncInitMethodName)) {
                asyncInitMethodManager.registerAsyncInitBean(beanFactory, beanName,
                        asyncInitMethodName);
            }
        }
    }

}
