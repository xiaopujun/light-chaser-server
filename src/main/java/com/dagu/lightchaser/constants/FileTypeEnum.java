package com.dagu.lightchaser.constants;

import lombok.Getter;

@Getter
public enum FileTypeEnum implements BaseWebParamEnum {
    IMAGE(1, "image"),
    VIDEO(2, "video"),
    MODEL(3, "model");

    private final int code;
    private final String name;

    FileTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

}
