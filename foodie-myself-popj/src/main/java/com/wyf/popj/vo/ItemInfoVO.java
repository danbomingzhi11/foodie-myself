package com.wyf.popj.vo;


import com.wyf.popj.Items;
import com.wyf.popj.ItemsImg;
import com.wyf.popj.ItemsParam;
import com.wyf.popj.ItemsSpec;
import lombok.Data;

import java.util.List;

/**
 * 商品详情VO
 */
@Data
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

}
