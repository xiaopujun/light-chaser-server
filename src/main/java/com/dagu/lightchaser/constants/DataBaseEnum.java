package com.dagu.lightchaser.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DataBaseEnum {
    MySQL(0, "MySQL"),
    SQLServer(1, "SQLServer"),
    ORACLE(2, "ORACLE"),
    PostgresSQL(3, "PostgresSQL"),
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
