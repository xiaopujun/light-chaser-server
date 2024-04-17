package com.dagu.lightchaser.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DataBaseEnum {
    MySQL(0, "MySQL"),
    PostgresSQL(1, "PostgresSQL"),
    ORACLE(2, "ORACLE"),
    SQLServer(3, "SQLServer"),
    ;

    @JsonValue
    @EnumValue
    private final int code;

    private final String name;

    DataBaseEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
