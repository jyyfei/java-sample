package com.sample.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author jiyunfei
 * @createTime 2021/11/8
 */
public class MapSortTest {
    public static void main(String[] args) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("cc", "111");
        hashMap.put("aa", "222");
        hashMap.put("ca", "111");
        hashMap.put("ac", "222");
        hashMap.put("cq", "111");
        hashMap.put("az", "222");
        hashMap.put("cs", "111");
        hashMap.put("as", "222");
        hashMap.put("cf", "111");
        hashMap.put("ag", "222");
        hashMap.put("ce", "111");
        hashMap.put("av", "222");
        hashMap.put("cg", "111");
        hashMap.put("ah", "222");
        hashMap.put("cn", "111");
        hashMap.put("aj", "222");
        System.out.println("HashMap");
        printMap(hashMap);

        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("cc", "111");
        linkedHashMap.put("aa", "222");
        System.out.println("LinkedHashMap");
        printMap(linkedHashMap);

        Map<String, String> treeMap = new TreeMap<>();
        treeMap.put("cc", "111");
        treeMap.put("aa", "222");
        System.out.println("TreeMap");
        printMap(treeMap);
    }

    private static void printMap(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
