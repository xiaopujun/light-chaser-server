package com.dagu.lightchaser.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dagu.lightchaser.constants.DataBaseEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据源管理
 */
@Data
@TableName(value = "datasource")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasourceEntity implements Serializable {
    /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数据源链接名称
     */
    private String name;

    /**
     * 数据源类型
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private DataBaseEnum type;

    /**
     * 用户名
     */
    private String username;

    /**
     * 数据源链接密码
     */
    private String password;

    /**
     * 数据源链接地址
     */
    private String url;

    /**
     * 数据源链接描述
     */
    private String des;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private int deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}