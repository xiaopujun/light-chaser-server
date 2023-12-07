package com.dagu.lightchaser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("project")
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
    private String prevUrl;


}
