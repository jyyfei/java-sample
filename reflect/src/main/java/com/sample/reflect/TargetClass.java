package com.sample.reflect;

import java.util.Collections;
import java.util.List;

public class TargetClass {
    public static final List<String> PRIVATE_STATIC_FINAL_FIELD = Collections.singletonList("Old Value");

    public static void get() {
        System.out.println(PRIVATE_STATIC_FINAL_FIELD);
    }
}