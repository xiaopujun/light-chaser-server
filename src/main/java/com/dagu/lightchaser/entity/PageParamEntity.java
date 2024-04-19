package com.dagu.lightchaser.entity;

import lombok.Data;

@Data
public class PageParamEntity {
    private int size;
    private int current;
    private String searchValue;
}
