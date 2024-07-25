package com.sample.javassist;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author yunfei.jyf
 * @date 2024/5/13
 */
public class JavassistTest {
    public static void main(String[] args) throws Exception {
        // 如果先加载TestClass在用javassist改写类会报错的，会class重复冲突
//        JavassistTest javassistTest = new JavassistTest();
//        javassistTest.test();

        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        String clsName = "com.sample.javassist.TestClass";
        CtClass ctClass = classPool.get(clsName);
        CtMethod ctMethod = ctClass.getDeclaredMethod("test");
        ctMethod.insertBefore("{System.out.println(\"test insert before\");\n}");
        ctMethod.insertAfter("{System.out.println(\"test insert after\");\n}");
        ctClass.toClass();
        // 释放对象
        ctClass.detach();
        new TestClass().test();

        JavassistTest javassistTest = new JavassistTest();
        javassistTest.test();
        Thread.sleep(1000 * 60);

//        CtClass ctClass = javassistTest.getCtClass(TestClass.class, TestClass.class.getClassLoader());
//        CtMethod ctMethod = ctClass.getDeclaredMethod("test");
//        ctMethod.insertBefore("{System.out.println(\"test insert before\");\n}");
//        ctMethod.insertAfter("{System.out.println(\"test insert after\");\n}");
//        ClassUtil.reDefine(TestClass.class, ctClass.toBytecode(), TestClass.class.getClassLoader());
//
//        Class<?> newClass = ctClass.toClass(TestClass.class.getClassLoader(), null);
//        TestClass newTestClass = (TestClass) newClass.newInstance();
//        System.out.println(newTestClass.test());
    }

    private void test() throws Exception {
        TestClass testClass = TestClass.class.newInstance();
        boolean test = testClass.test();
        System.out.println(test);
    }

    public CtClass getCtClass(Class sourceClass, ClassLoader loader) throws IOException {
        final ClassPool classPool = new ClassPool(true);
        if (loader == null) {
            classPool.appendClassPath(new LoaderClassPath(ClassLoader.getSystemClassLoader()));
        } else {
            classPool.appendClassPath(new LoaderClassPath(loader));
        }

        byte[] classFileBuffer = ClassUtil.getClassFileBuffer(sourceClass);
        final CtClass clazz = classPool.makeClass(new ByteArrayInputStream(classFileBuffer), false);
        clazz.defrost();

        return clazz;
    }
}
