package com.wyf.enums;


public enum Sex {
    MAN(1, "男"),
    WOMAN(0, "女"),
    SECRET(2, "秘密");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
