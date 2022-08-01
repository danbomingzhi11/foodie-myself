package com.wyf.mapper;

import com.wyf.my.mapper.MyMapper;
import com.wyf.popj.Category;
import com.wyf.popj.vo.SelectAllGrade;
import com.wyf.popj.vo.SixItemsVo;

import java.util.List;

public interface CategoryMapper extends MyMapper<Category> {
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