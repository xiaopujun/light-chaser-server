package com.dagu.lightchaser.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("project")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String des;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int status;
    private int saveType;
    @TableLogic
    private int deleted;
    private String dataJson;
    private String cover;
    @TableField(exist = false)
    private MultipartFile file;
}
