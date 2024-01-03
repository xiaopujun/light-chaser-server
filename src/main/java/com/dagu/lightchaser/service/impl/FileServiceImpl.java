package com.dagu.lightchaser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dagu.lightchaser.dao.ImageDao;
import com.dagu.lightchaser.entity.ImageEntity;
import com.dagu.lightchaser.global.AppException;
import com.dagu.lightchaser.global.GlobalVariables;
import com.dagu.lightchaser.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {

    @Resource
    private ImageDao imageDao;

    @Override
    public String uploadImage(ImageEntity imageEntity) {
        //校验基础参数
        if (imageEntity == null || imageEntity.getProjectId() == null || imageEntity.getFile() == null)
            throw new AppException(500, "图片文件参数错误");
        //校验文件格式、大小
        MultipartFile file = imageEntity.getFile();
        if (file.getSize() > GlobalVariables.IMAGE_SIZE)
            throw new AppException(500, "图片大小不能超过5M");
        String fileName = file.getOriginalFilename();
        if (fileName == null)
            throw new AppException(500, "图片名称错误");
        //设置文件原始名称
        imageEntity.setName(fileName);
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (!Arrays.asList(GlobalVariables.IMAGE_TYPE).contains(suffix))
            throw new AppException(500, "图片格式不支持");

        //校验文件hash
        String hash;
        try {
            hash = DigestUtils.md5DigestAsHex(file.getInputStream());
        } catch (IOException e) {
            throw new AppException(500, "图片hash校验失败");
        }
        LambdaQueryWrapper<ImageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ImageEntity::getProjectId, imageEntity.getProjectId())
                .eq(ImageEntity::getHash, hash)
                .eq(ImageEntity::getDeleted, 0);
        ImageEntity record = imageDao.selectOne(queryWrapper);
        //如果已经存在相同hash值的图片文件，则直接返回已存在的图片地址
        if (record != null)
            return GlobalVariables.IMAGE_PATH + record.getUrl();

        //生成文件路径、文件名
        fileName = UUID.randomUUID() + suffix;
        //保存文件
        File uploadDir = new File(GlobalVariables.PROJECT_PATH + GlobalVariables.IMAGE_PATH);
        if (!uploadDir.exists()) {
            boolean mkdirs = uploadDir.mkdirs();
            if (!mkdirs)
                throw new AppException(500, "文件上传目录创建失败");
        }
        File destFile = new File(uploadDir, fileName);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new AppException(500, "图片写入文件系统失败");
        }
        //数据入库
        imageEntity.setUrl(fileName);
        imageEntity.setHash(hash);
        imageDao.insert(imageEntity);
        //返回文件路径
        return GlobalVariables.IMAGE_PATH + imageEntity.getUrl();
    }

    @Override
    public List<ImageEntity> getImageSourceList(String projectId) {
        if (projectId == null || projectId.equals(""))
            throw new AppException(500, "项目id错误");
        QueryWrapper<ImageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ImageEntity::getProjectId, projectId).eq(ImageEntity::getDeleted, 0);
        List<ImageEntity> images = imageDao.selectList(queryWrapper);
        images.forEach((image) -> {
            image.setUrl(GlobalVariables.IMAGE_PATH + image.getUrl());
        });
        return images;
    }

}
