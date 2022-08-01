package com.wyf.service.impl;

import com.wyf.mapper.ItemsImgMapper;
import com.wyf.popj.ItemsImg;
import com.wyf.service.ItemsImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;

@Service
public class ItemsImgServiceImpl implements ItemsImgService {

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    //根据itemId 查询到对应的itemImg表的数据
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsImg selectImgMessageByItemId(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        criteria.andEqualTo("isMain", 1);
        return itemsImgMapper.selectOneByExample(example);
    }
}
