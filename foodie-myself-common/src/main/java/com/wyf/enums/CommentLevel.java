package com.wyf.enums;

public enum CommentLevel {
    GOOD(1, "好评"),
    SIMPLE(2, "中评"),
    BAD(3, "差评");

    public final Integer type;
    public final String value;

    CommentLevel(Integer type, String value) {
        this.value = value;
        this.type = type;
    }

    }
