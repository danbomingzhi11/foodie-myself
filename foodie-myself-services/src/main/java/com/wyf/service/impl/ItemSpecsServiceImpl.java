package com.wyf.service.impl;

import com.wyf.mapper.ItemsSpecMapper;
import com.wyf.popj.ItemsSpec;
import com.wyf.service.ItemsSpecService;
import io.swagger.annotations.ApiOperation;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemSpecsServiceImpl implements ItemsSpecService {

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    // 根据SpecId 查询对应的Spec表的数据
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsSpec selectSpecBySpecId(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    // 创建订单完成 删除对应规格表里的库存量
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer decreaseItemSpecStock(String specId, Integer buyCounts, Integer stockCount) {
        Integer newStockCount = stockCount - buyCounts;
        if (newStockCount < 0) {
            return 0;
        }
        ItemsSpec itemsSpec = new ItemsSpec();
        itemsSpec.setId(specId);
        itemsSpec.setStock(newStockCount);
        return itemsSpecMapper.updateByPrimaryKeySelective(itemsSpec);
    }
}
