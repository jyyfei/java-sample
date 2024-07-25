package com.spring.demo.copy;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SourceDTO implements Serializable {
    private static final long serialVersionUID = -2816184136491641241L;

    private List<String> sysList;
    private List<ItemDTO> itemList;

    private Long appId;

    private String appName;
}
