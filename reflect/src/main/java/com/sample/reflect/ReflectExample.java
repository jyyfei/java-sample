package com.sample.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;

/**
 * 通过反射修改静态final字段
 * TODO com.sample.reflect.TargetClass#PRIVATE_STATIC_FINAL_FIELD为String类型时不符合预期
 *
 * Field.class.getDeclaredField("modifiers")在Java17版本时会报错
 */
public class ReflectExample {
    public static void main(String[] args) throws Exception {
        TargetClass.get();

        Field field = TargetClass.class.getDeclaredField("PRIVATE_STATIC_FINAL_FIELD");
        field.setAccessible(true);

        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        // remove final modifiers
        // 去掉final修饰符
        int oldModifiers = field.getModifiers();
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // 修改字段的值
        field.set(null, Collections.singletonList("New Value"));
        System.out.println(field.get(null));

        // recover final modifiers
        // 把final修饰符恢复回来
        modifiers.setInt(field, oldModifiers);

        // 输出修改后的值
        TargetClass.get();

    }
}
