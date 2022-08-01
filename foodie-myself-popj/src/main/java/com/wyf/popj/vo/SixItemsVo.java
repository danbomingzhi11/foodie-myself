package com.wyf.popj.vo;

import lombok.Data;

import java.util.List;

@Data
public class SixItemsVo {


    private Integer rootCatId;

    private String rootCatName;

    private String slogan;

    private String catImage;

    private String bgColor;

    private List<SixItemImageVo> simpleItemList;

}
