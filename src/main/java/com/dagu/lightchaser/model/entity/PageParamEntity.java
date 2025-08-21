package com.dagu.lightchaser.model.entity;

import lombok.Data;

@Data
public class PageParamEntity {
    private Long size;
    private Long current;
    private String searchValue;
}
