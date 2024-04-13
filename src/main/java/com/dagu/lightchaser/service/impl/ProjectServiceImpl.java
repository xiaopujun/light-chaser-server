package com.dagu.lightchaser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dagu.lightchaser.dao.ProjectMapper;
import com.dagu.lightchaser.entity.ProjectEntity;
import com.dagu.lightchaser.global.AppException;
import com.dagu.lightchaser.global.GlobalVariables;
import com.dagu.lightchaser.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public Boolean updateProject(ProjectEntity project) {
        if (project == null || project.getId() == null)
            return false;
        project.setUpdateTime(LocalDateTime.now());
        return projectMapper.updateById(project) > 0;
    }

    @Override
    public List<ProjectEntity> getProjectList() {
        LambdaQueryWrapper<ProjectEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ProjectEntity::getId, ProjectEntity::getName, ProjectEntity::getDes, ProjectEntity::getCover);
        List<ProjectEntity> projectEntities = projectMapper.selectList(queryWrapper);
        //补全封面的完整路径
        for (ProjectEntity projectEntity : projectEntities) {
            if (projectEntity.getCover() != null) {
                projectEntity.setCover(GlobalVariables.COVER_PATH + projectEntity.getCover());
            }
        }
        return projectEntities;
    }

    @Override
    public String getProjectData(Long id) {
        if (id == null)
            return null;
        LambdaQueryWrapper<ProjectEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ProjectEntity::getDataJson).eq(ProjectEntity::getId, id);
        return projectMapper.selectOne(queryWrapper).getDataJson();
    }

    @Override
    public Long createProject(ProjectEntity project) {
        if (project == null)
            return null;
        projectMapper.insert(project);
        return project.getId();
    }

    @Override
    public Boolean deleteProject(Long id) {
        if (id == null)
            return false;
        projectMapper.deleteById(id);
        return true;
    }

    @Override
    public Long copyProject(Long id) {
        if (id == null)
            return null;
        ProjectEntity project = projectMapper.selectById(id);
        if (project == null)
            return null;
        project.setId(null);
        project.setName(project.getName() + " - 副本");
        project.setCreateTime(null);
        project.setUpdateTime(null);
        projectMapper.insert(project);
        return project.getId();
    }

    @Override
    public ProjectEntity getProjectInfo(Long id) {
        if (id == null)
            return null;
        return projectMapper.selectById(id);
    }

    @Override
    public String uploadCover(ProjectEntity project) {
        if (project == null || project.getId() == null || project.getFile() == null)
            throw new AppException(500, "参数错误");
        MultipartFile file = project.getFile();
        if (file.getSize() > GlobalVariables.IMAGE_SIZE)
            throw new AppException(500, "图片大小不能超过5M");
        String fileName = file.getOriginalFilename();
        if (fileName == null)
            throw new AppException(500, "图片名称错误");
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (!Arrays.asList(GlobalVariables.IMAGE_TYPE).contains(suffix))
            throw new AppException(500, "图片格式不支持");
        LambdaQueryWrapper<ProjectEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectEntity::getId, project.getId())
                .eq(ProjectEntity::getDeleted, 0);
        ProjectEntity record = projectMapper.selectOne(queryWrapper);
        if (record != null && record.getCover() != null) {
            String oldFileName = record.getCover();
            String oldAbsolutePath = GlobalVariables.PROJECT_RESOURCE_PATH + GlobalVariables.COVER_PATH + oldFileName;
            //删除旧文件
            File oldFile = new File(oldAbsolutePath);
            if (oldFile.exists()) {
                boolean delete = oldFile.delete();
                if (!delete)
                    throw new AppException(500, "旧图片删除失败");
            }
        }
        //生成文件路径、文件名
        fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        //保存文件
        File uploadDir = new File(GlobalVariables.PROJECT_RESOURCE_PATH + GlobalVariables.COVER_PATH);
        if (!uploadDir.exists()) {
            boolean mkdirs = uploadDir.mkdirs();
            if (!mkdirs)
                throw new AppException(500, "封面目录创建失败");
        }
        File destFile = new File(uploadDir, fileName);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new AppException(500, "图片写入文件系统失败");
        }
        //数据入库
        project.setCover(fileName);
        project.setUpdateTime(LocalDateTime.now());
        projectMapper.updateById(project);
        return GlobalVariables.COVER_PATH + fileName;
    }
}
