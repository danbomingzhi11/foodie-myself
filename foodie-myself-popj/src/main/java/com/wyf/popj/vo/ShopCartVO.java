package com.wyf.popj.vo;

import lombok.Data;

@Data
public class ShopCartVO {

    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private String priceDiscount;
    private String priceNormal;

    public String getItemId() {
        return itemId;
    }


}
