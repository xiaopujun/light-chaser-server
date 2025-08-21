package com.dagu.lightchaser.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dagu.lightchaser.model.constants.FileTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@TableName("file")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 图片路径，数据库仅存储路径片段，完整路径需要读取数据库片段后结合配置完整拼接后得出
     */
    private String url;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private FileTypeEnum type;
    private LocalDateTime createTime;
    @TableLogic
    private int deleted;
    private Long projectId;
    private String hash;
    @TableField(exist = false)
    private MultipartFile file;
}
