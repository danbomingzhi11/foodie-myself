package com.wyf.service;

import com.wyf.popj.ItemsSpec;

public interface ItemsSpecService {
    /**
     * 根据SpecId 查询对应的Spec表的数据
     * @return
     * @param specId
     */
    ItemsSpec selectSpecBySpecId(String specId);

    /**
     * 创建订单完成 删除对应规格表里的库存量
     * @param specId
     * @param buyCounts
     * @return
     */
    Integer decreaseItemSpecStock(String specId, Integer buyCounts, Integer stockCount);
}
