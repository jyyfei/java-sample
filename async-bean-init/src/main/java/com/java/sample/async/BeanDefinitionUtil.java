package com.java.sample.async;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class BeanDefinitionUtil {

    public static Class<?> resolveBeanClassType(BeanDefinition beanDefinition) {
        Class<?> clazz = null;

        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            String className;
            if (isFromConfigurationSource(beanDefinition)) {
                MethodMetadata methodMetadata = ((AnnotatedBeanDefinition) beanDefinition)
                        .getFactoryMethodMetadata();
                className = methodMetadata.getReturnTypeName();
            } else {
                AnnotationMetadata annotationMetadata = ((AnnotatedBeanDefinition) beanDefinition)
                        .getMetadata();
                className = annotationMetadata.getClassName();
            }

            try {
                if (StringUtils.hasText(className)) {
                    clazz = ClassUtils.forName(className, null);
                }
            } catch (Throwable throwable) {
                // ignore
            }
        }

        if (clazz == null) {
            try {
                clazz = ((AbstractBeanDefinition) beanDefinition).getBeanClass();
            } catch (IllegalStateException ex) {
                try {
                    String className = beanDefinition.getBeanClassName();
                    if (StringUtils.hasText(className)) {
                        clazz = ClassUtils.forName(className, null);
                    }
                } catch (Throwable throwable) {
                    // ignore
                }
            }
        }

        if (clazz == null) {
            if (beanDefinition instanceof RootBeanDefinition) {
                clazz = ((RootBeanDefinition) beanDefinition).getTargetType();
            }
        }

        if (ClassUtils.isCglibProxyClass(clazz)) {
            return clazz.getSuperclass();
        } else {
            return clazz;
        }
    }

    public static boolean isFromConfigurationSource(BeanDefinition beanDefinition) {
        return beanDefinition
                .getClass()
                .getCanonicalName()
                .startsWith(
                        "org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader");
    }
}
