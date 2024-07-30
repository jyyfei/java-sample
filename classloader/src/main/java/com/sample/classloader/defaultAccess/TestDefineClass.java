package com.sample.classloader.defaultAccess;

import com.sample.classloader.ClassUtil;

/**
 * 当B是默认访问修饰符的时候一下代码创建B对象时会报错tried to access class com.sample.classloader.defaultAccess.BClass from class com.sample.classloader.defaultAccess.AClass
 * B为public时则不会报错
 * B的类加载器一定是Launcher，为什么？？？
 *
 * @author yunfei.jyf
 * @date 2024/4/16
 */
public class TestDefineClass {
    public static void main(String[] args) throws Exception {
        AClass aClass = new AClass();
        ClassLoader urlClassLoader = ClassUtil.newClassloader("/tmp/");

        System.out.println(urlClassLoader);
        Thread.currentThread().setContextClassLoader(urlClassLoader);
//        ClassUtil.reDefine(BClass.class, Thread.currentThread().getContextClassLoader());
        Class reDefine = ClassUtil.reDefine(AClass.class, Thread.currentThread().getContextClassLoader());
        Object aa = reDefine.newInstance();
        System.out.println(aa.getClass().getClassLoader());
    }
}
