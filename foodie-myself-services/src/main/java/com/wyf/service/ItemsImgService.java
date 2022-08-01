package com.wyf.service;

import com.wyf.popj.ItemsImg;

public interface ItemsImgService {
    /**
     * 根据itemId 查询到对应的itemImg表的数据
     * @return
     * @param itemId
     */
    ItemsImg selectImgMessageByItemId(String itemId);
}
