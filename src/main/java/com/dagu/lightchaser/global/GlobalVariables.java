package com.dagu.lightchaser.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalVariables {

    /**
     * 可接收的图片类型
     */
    public static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".png", ".gif"};

    /**
     * 图片大小限制
     */
    public static final long IMAGE_SIZE = 1024 * 1024 * 5;

    /**
     * 项目资源路径，用于存储本项目相关的外部资源文件
     */
    public static String PROJECT_RESOURCE_PATH;

    /**
     * 图片资源路径，用于存储图片资源
     */
    public static String SOURCE_IMAGE_PATH;

    /**
     * 封面路径，用于存储项目封面图片
     */
    public static String COVER_PATH;

    @Value("${light-chaser.project-resource-path}")
    public void setProjectResourcePath(String path) {
        PROJECT_RESOURCE_PATH = path;
    }

    @Value("${light-chaser.source-image-path}")
    public void setSourceImagePath(String path) {
        SOURCE_IMAGE_PATH = path;
    }

    @Value("${light-chaser.cover-path}")
    public void setCoverPath(String path) {
        COVER_PATH = path;
    }
}
