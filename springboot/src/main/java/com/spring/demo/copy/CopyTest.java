package com.spring.demo.copy;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.DebuggingClassWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author JiYunFei
 * @date 2023/3/2
 */
public class CopyTest {
    public static void main(String[] args) {
        // cglibd动态生成的class打印到此路径下
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "spring-demo/classPath/");

        SourceDTO sourceDTO = new SourceDTO();
        List<String> qqq = new ArrayList<>();
        qqq.add("QQQ");
        sourceDTO.setSysList(qqq);
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setA(1);
        sourceDTO.setItemList(Collections.singletonList(itemDTO));

        TargetVO targetVO = new TargetVO();
        BeanUtils.copyProperties(sourceDTO, targetVO);
        targetVO.getSysList().add(1);

        String s = String.valueOf(targetVO.getSysList().get(0));
        System.out.println(s);



        // integers返回的实际是内部类的List，没有实现Add
        List<Integer> integers = Arrays.asList(1);
//        Exception in thread "main" java.lang.UnsupportedOperationException
//        integers.add(12);
        System.out.println(integers);

//        BeanCopier beanCopier = BeanCopier.create(SourceDTO.class, TargetVO.class, false);
//        TargetVO targetVO2 = new TargetVO();
//        beanCopier.copy(sourceDTO, targetVO2, null);
//        System.out.println(targetVO2.getSysList().get(0).getClass().getName());
    }
}
