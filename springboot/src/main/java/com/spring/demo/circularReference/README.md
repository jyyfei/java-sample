复现spring aop 代理类导致循环依赖问题

```
org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'serviceA': Bean with name 'serviceA' has been injected into other beans [serviceB] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching - consider using 'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.
```