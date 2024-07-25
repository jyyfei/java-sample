package com.sample.classloader.defaultAccess;

import com.sample.classloader.ClassUtil;

/**
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
        System.out.println(aa);
    }
}
