package com.wyf.enums;

/**
 * @Desc: 是否枚举类
 */
public enum LevelEnums {
    good(1, "yes"),

    normal(2,"no"),

    bad(3,"no");


    public final Integer type;

    public final String value;

    LevelEnums(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
