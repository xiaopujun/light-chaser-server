package com.dagu.lightchaser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@TableName("source_image")
public class ImageEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String url;
    private String name;
    private LocalDateTime createTime;
    private int deleted;
    private Long projectId;
    private String hash;
    @TableField(exist = false)
    private MultipartFile file;
}
