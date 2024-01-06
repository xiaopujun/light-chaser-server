package com.dagu.lightchaser.service;

import com.dagu.lightchaser.entity.SourceImageEntity;

import java.util.List;

public interface SourceImageService {
    /**
     * 上传图片
     *
     * @param sourceImageEntity 图片实体
     * @return 图片地址
     */
    String uploadImage(SourceImageEntity sourceImageEntity);

    /**
     * 获取指定项目下的所有手动上传的图片资源
     *
     * @param projectId 项目id
     * @return 图片资源列表
     */
    List<SourceImageEntity> getSourceImageList(Long projectId);

    /**
     * 删除指定图片资源
     *
     * @param imageId 图片id
     * @return 是否删除成功
     */
    Boolean delImageSource(Long imageId);
}
