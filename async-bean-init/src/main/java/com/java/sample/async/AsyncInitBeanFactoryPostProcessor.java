package com.java.sample.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 给AsyncInit标注的bean的beanDefinition添加async-init-method-name属性
 */
@Slf4j
public class AsyncInitBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Arrays.stream(beanFactory.getBeanDefinitionNames())
                .collect(Collectors.toMap(Function.identity(), beanFactory::getBeanDefinition))
                .forEach(this::scanAsyncInitBeanDefinition);
    }

    private void scanAsyncInitBeanDefinition(String beanId, BeanDefinition beanDefinition) {
        if (BeanDefinitionUtil.isFromConfigurationSource(beanDefinition)) {
            scanAsyncInitBeanDefinitionOnMethod(beanId, (AnnotatedBeanDefinition) beanDefinition);
        } else {
            Class<?> beanClassType = BeanDefinitionUtil.resolveBeanClassType(beanDefinition);
            if (beanClassType == null) {
                return;
            }
            scanAsyncInitBeanDefinitionOnClass(beanClassType, beanDefinition);
        }
    }

    private void scanAsyncInitBeanDefinitionOnMethod(String beanId,
                                                     AnnotatedBeanDefinition beanDefinition) {
        Class<?> returnType;
        Class<?> declaringClass;
        List<Method> candidateMethods = new ArrayList<>();

        MethodMetadata methodMetadata = beanDefinition.getFactoryMethodMetadata();
        try {
            returnType = ClassUtils.forName(methodMetadata.getReturnTypeName(), null);
            declaringClass = ClassUtils.forName(methodMetadata.getDeclaringClassName(), null);
        } catch (Throwable throwable) {
            // it's impossible to catch throwable here
            log.error("01-02001 " + beanId, throwable);
            return;
        }
        if (methodMetadata instanceof StandardMethodMetadata) {
            candidateMethods.add(((StandardMethodMetadata) methodMetadata).getIntrospectedMethod());
        } else {
            for (Method m : declaringClass.getDeclaredMethods()) {
                // check methodName and return type
                if (!m.getName().equals(methodMetadata.getMethodName())
                        || !m.getReturnType().getTypeName().equals(methodMetadata.getReturnTypeName())) {
                    continue;
                }

                // check bean method
                if (!AnnotatedElementUtils.hasAnnotation(m, Bean.class)) {
                    continue;
                }

                Bean bean = m.getAnnotation(Bean.class);
                Set<String> beanNames = new HashSet<>();
                beanNames.add(m.getName());
                if (bean != null) {
                    beanNames.addAll(Arrays.asList(bean.name()));
                    beanNames.addAll(Arrays.asList(bean.value()));
                }

                // check bean name
                if (!beanNames.contains(beanId)) {
                    continue;
                }

                candidateMethods.add(m);
            }
        }

        if (candidateMethods.size() == 1) {
            AsyncInit asyncInitAnnotation = candidateMethods.get(0).getAnnotation(AsyncInit.class);
            if (asyncInitAnnotation == null) {
                asyncInitAnnotation = returnType.getAnnotation(AsyncInit.class);
            }
            registerAsyncInitBean(asyncInitAnnotation, beanDefinition);
        } else if (candidateMethods.size() > 1) {
            for (Method m : candidateMethods) {
                if (AnnotatedElementUtils.hasAnnotation(m, AsyncInit.class)
                        || AnnotatedElementUtils.hasAnnotation(returnType, AsyncInit.class)) {
                    throw new FatalBeanException("01-02002" + declaringClass.getCanonicalName());
                }
            }
        }
    }

    private void scanAsyncInitBeanDefinitionOnClass(Class<?> beanClass,
                                                    BeanDefinition beanDefinition) {
        // See issue: https://github.com/sofastack/sofa-boot/issues/835
        AsyncInit sofaAsyncInitAnnotation = AnnotationUtils.findAnnotation(beanClass, AsyncInit.class);
        registerAsyncInitBean(sofaAsyncInitAnnotation, beanDefinition);
    }

    private void registerAsyncInitBean(AsyncInit sofaAsyncInitAnnotation, BeanDefinition beanDefinition) {
        if (sofaAsyncInitAnnotation == null) {
            return;
        }

        String initMethodName = beanDefinition.getInitMethodName();
        if (sofaAsyncInitAnnotation.value() && StringUtils.hasText(initMethodName)) {
            beanDefinition.setAttribute(AsyncInitMethodManager.ASYNC_INIT_METHOD_NAME, initMethodName);
        }
    }
}
