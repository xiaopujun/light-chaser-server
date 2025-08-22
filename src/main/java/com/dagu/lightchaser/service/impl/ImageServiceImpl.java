package com.dagu.lightchaser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dagu.lightchaser.global.AppException;
import com.dagu.lightchaser.global.GlobalProperties;
import com.dagu.lightchaser.mapper.ImageMapper;
import com.dagu.lightchaser.model.dto.ImageDTO;
import com.dagu.lightchaser.model.po.ImagePO;
import com.dagu.lightchaser.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl extends ServiceImpl<ImageMapper, ImagePO> implements ImageService {

    private final GlobalProperties globalProperties;

    @Override
    @Transactional
    public String uploadImage(ImageDTO imageDTO) {
        //校验基础参数
        if (imageDTO == null || imageDTO.getFile() == null)
            throw new AppException(500, "图片文件参数错误");
        //校验文件格式、大小
        MultipartFile file = imageDTO.getFile();
        if (file.getSize() > GlobalProperties.IMAGE_SIZE)
            throw new AppException(500, "图片大小不能超过5M");
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null)
            throw new AppException(500, "图片名称错误");
        //设置文件原始名称
        imageDTO.setName(originalFilename);
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!Arrays.asList(GlobalProperties.IMAGE_TYPE).contains(suffix))
            throw new AppException(500, "图片格式不支持");

        //生成文件路径、文件名
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        //保存文件
        File uploadDir = new File(globalProperties.getImageAbsolutPath());
        if (!uploadDir.exists()) {
            boolean mkdirs = uploadDir.mkdirs();
            if (!mkdirs)
                throw new AppException(500, "文件上传目录创建失败");
        }
        File destFile = new File(uploadDir, newFileName);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new AppException(500, "图片写入文件系统失败");
        }

        ImagePO imagePO = new ImagePO().setUrl(newFileName).setName(originalFilename);
        this.save(imagePO);
        //返回文件路径
        return Path.of(globalProperties.getImagePath(), newFileName).toString();
    }

    @Override
    @Transactional
    public Boolean batchDeleteImage(List<Long> imageIdList) {
        if (imageIdList == null || imageIdList.isEmpty())
            throw new AppException(500, "删除错误：图片ID列表为空");
        return removeBatchByIds(imageIdList);
    }
}
