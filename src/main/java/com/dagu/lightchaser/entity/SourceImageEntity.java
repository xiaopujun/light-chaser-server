package com.dagu.lightchaser.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@TableName("source_image")
public class SourceImageEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String url;
    private String name;
    private LocalDateTime createTime;
    @TableLogic
    private int deleted;
    private Long projectId;
    private String hash;
    @TableField(exist = false)
    private MultipartFile file;
}
