package com.spring.demo.copy;

import lombok.Data;

import java.util.List;

@Data
public class TargetVO {
    private List<Integer> sysList;
//    private List sysList;
    private List<ItemVO> itemList;

    private Long appId;

    private String appName;
}
