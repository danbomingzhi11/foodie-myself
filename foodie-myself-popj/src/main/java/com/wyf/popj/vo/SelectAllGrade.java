package com.wyf.popj.vo;

import lombok.Data;

import java.util.List;

@Data
public class SelectAllGrade {

    private Integer id;

    private String name;

    private String type;

    private Integer fatherId;

    private List<SubCategoryVO> subCatList;
}
