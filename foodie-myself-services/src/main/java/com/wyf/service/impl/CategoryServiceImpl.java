package com.wyf.service.impl;

import com.wyf.mapper.CategoryMapper;
import com.wyf.popj.Carousel;
import com.wyf.popj.Category;
import com.wyf.popj.vo.SelectAllGrade;
import com.wyf.popj.vo.SixItemsVo;
import com.wyf.service.CategoryService;
import com.wyf.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    //查询所有的一级分类
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Category> selectFirstGradeCarts() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fatherId", 0);

        List<Category> categoryList = categoryMapper.selectByExample(example);

        return categoryList;
    }

    //根据父ID查询 六条Item表的数据
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<SixItemsVo> selectSixItemsByRootCatId(Integer rootCatId) {
        List<SixItemsVo> sixItemsVoList = categoryMapper.selectSixItemsByRootCatId(rootCatId);

        return sixItemsVoList;
    }


    //根据一级ID查询所有的子类
    @Override
    public List<SelectAllGrade> selectAllGradeById(Integer id) {
        List<SelectAllGrade> selectAllGradeList = categoryMapper.selectAllGradeById(id);

        return selectAllGradeList;
    }


}
