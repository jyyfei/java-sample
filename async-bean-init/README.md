提供spring bean异步执行init方法的能力
参考sofa-boot的实现
https://www.sofastack.tech/projects/sofa-boot/speed-up-startup/


1、bean创建前扫描所有的BeanDefinition判断是否存在AsyncInit注解以及init方法，存在向BeanDefinition上添加特殊属性
com.java.sample.async.AsyncInitBeanFactoryPostProcessor.registerAsyncInitBean

2、bean需要执行init方法前将bean包装成代理类，代理类中切init方法，当init方法执行时启动异步线程执行，直接return null
com.java.sample.async.AsyncProxyBeanPostProcessor.postProcessBeforeInitialization