package com.dagu.lightchaser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dagu.lightchaser.dao.SourceImageDao;
import com.dagu.lightchaser.entity.SourceImageEntity;
import com.dagu.lightchaser.global.AppException;
import com.dagu.lightchaser.global.GlobalVariables;
import com.dagu.lightchaser.service.SourceImageService;
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
public class SourceImageServiceImpl implements SourceImageService {

    @Resource
    private SourceImageDao sourceImageDao;

    @Override
    public String uploadImage(SourceImageEntity sourceImageEntity) {
        //校验基础参数
        if (sourceImageEntity == null || sourceImageEntity.getProjectId() == null || sourceImageEntity.getFile() == null)
            throw new AppException(500, "图片文件参数错误");
        //校验文件格式、大小
        MultipartFile file = sourceImageEntity.getFile();
        if (file.getSize() > GlobalVariables.IMAGE_SIZE)
            throw new AppException(500, "图片大小不能超过5M");
        String fileName = file.getOriginalFilename();
        if (fileName == null)
            throw new AppException(500, "图片名称错误");
        //设置文件原始名称
        sourceImageEntity.setName(fileName);
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
        LambdaQueryWrapper<SourceImageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SourceImageEntity::getProjectId, sourceImageEntity.getProjectId())
                .eq(SourceImageEntity::getHash, hash)
                .eq(SourceImageEntity::getDeleted, 0);
        SourceImageEntity record = sourceImageDao.selectOne(queryWrapper);
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
        sourceImageEntity.setUrl(fileName);
        sourceImageEntity.setHash(hash);
        sourceImageDao.insert(sourceImageEntity);
        //返回文件路径
        return GlobalVariables.IMAGE_PATH + sourceImageEntity.getUrl();
    }

    @Override
    public List<SourceImageEntity> getSourceImageList(Long projectId) {
        if (projectId == null || projectId <= 0)
            throw new AppException(500, "项目id错误");
        QueryWrapper<SourceImageEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SourceImageEntity::getProjectId, projectId).eq(SourceImageEntity::getDeleted, 0);
        List<SourceImageEntity> images = sourceImageDao.selectList(queryWrapper);
        images.forEach((image) -> image.setUrl(GlobalVariables.IMAGE_PATH + image.getUrl()));
        return images;
    }

    @Override
    public Boolean delImageSource(Long imageId) {
        if (imageId == null || imageId <= 0)
            throw new AppException(500, "图片id错误");
        int row = sourceImageDao.deleteById(imageId);
        return row > 0;
    }

}
