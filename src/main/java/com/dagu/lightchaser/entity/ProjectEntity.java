package com.dagu.lightchaser.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ProjectEntity implements Serializable {
    private Long id;
    private String name;
    private String des;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int status;
    private int saveType;
    private int del;
    private String dataJson;
}
