package com.dagu.lightchaser.service;

import com.dagu.lightchaser.entity.ImageEntity;

import java.util.List;

public interface FileService {
    /**
     * 上传图片
     *
     * @param imageEntity 图片实体
     * @return 图片地址
     */
    String uploadImage(ImageEntity imageEntity);

    /**
     * 获取指定项目下的所有手动上传的图片资源
     *
     * @param projectId 项目id
     * @return 图片资源列表
     */
    List<ImageEntity> getImageSourceList(String projectId);
}
