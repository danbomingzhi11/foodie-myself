package com.wyf.service;

import com.wyf.popj.Category;
import com.wyf.popj.vo.SelectAllGrade;
import com.wyf.popj.vo.SixItemsVo;

import java.util.List;

public interface CategoryService {

    /**
     * 查询所有的一级分类
     * @return
     */
    List<Category> selectFirstGradeCarts();

    /**
     * 根据父ID查询 六条Item表的数据
     * @param rootCatId
     * @return
     */
    List<SixItemsVo> selectSixItemsByRootCatId(Integer rootCatId);

    /**
     * 根据一级ID查询所有的子类
     * @return
     */
    List<SelectAllGrade> selectAllGradeById(Integer id);
}
