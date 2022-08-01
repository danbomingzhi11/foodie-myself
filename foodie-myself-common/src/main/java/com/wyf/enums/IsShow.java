package com.wyf.enums;

public enum IsShow {
    YES("YES", 1),
    NO("No", 2);

    public final String type;
    public final Integer isShow;

    IsShow(String type, Integer isShow){
        this.type = type;
        this.isShow = isShow;
    }
}
