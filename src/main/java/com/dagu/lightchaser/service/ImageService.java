package com.dagu.lightchaser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dagu.lightchaser.model.dto.ImageDTO;
import com.dagu.lightchaser.model.po.ImagePO;

import java.util.List;

public interface ImageService extends IService<ImagePO> {
    /**
     * 上传图片
     *
     * @param imageDTO 图片实体
     * @return 图片地址
     */
    String uploadImage(ImageDTO imageDTO);


    /**
     * 批量删除图片资源
     *
     * @param imageIdList 图片id列表
     * @return 是否删除成功
     */
    Boolean batchDeleteImage(List<Long> imageIdList);
}
