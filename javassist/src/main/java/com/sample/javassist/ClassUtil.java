package com.sample.javassist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;

/**
 * @author yunfei.jyf
 * @date 2024/4/22
 */
public class ClassUtil {
    public static Class reDefine(Class clazz, byte[] classFileBuffer, ClassLoader classLoader) throws Exception {
        Method mdDefineClass = ClassLoader.class
                .getDeclaredMethod("defineClass",
                        String.class, byte[].class, int.class, int.class,
                        ProtectionDomain.class);
        mdDefineClass.setAccessible(true);

        Class c1 = (Class) mdDefineClass.invoke(classLoader, new Object[]{
                clazz.getName(), classFileBuffer, 0, classFileBuffer.length, clazz.getProtectionDomain()
        });
        return c1;
    }

    public static byte[] getClassFileBuffer(Class<?> clazz) throws IOException {
        String classFilePath = clazz.getName().replace('.', '/') + ".class";
        try (InputStream is = clazz.getClassLoader().getResourceAsStream(classFilePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }
}
