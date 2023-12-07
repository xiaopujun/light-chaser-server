package com.dagu.lightchaser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dagu.lightchaser.dao.ProjectDao;
import com.dagu.lightchaser.entity.ProjectEntity;
import com.dagu.lightchaser.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectDao projectDao;

    @Override
    public Boolean updateProject(ProjectEntity project) {
        if (project == null || project.getId() == null)
            return false;
        project.setUpdateTime(LocalDateTime.now());
        return projectDao.updateById(project) > 0;
    }

    @Override
    public List<ProjectEntity> getProjectList() {
        LambdaQueryWrapper<ProjectEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ProjectEntity::getId, ProjectEntity::getName, ProjectEntity::getSaveType, ProjectEntity::getDes, ProjectEntity::getStatus, ProjectEntity::getPrevUrl);
        return projectDao.selectList(queryWrapper);
    }

    @Override
    public String getProjectData(Long id) {
        if (id == null)
            return null;
        LambdaQueryWrapper<ProjectEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ProjectEntity::getDataJson).eq(ProjectEntity::getId, id);
        return projectDao.selectOne(queryWrapper).getDataJson();
    }

    @Override
    public Long createProject(ProjectEntity project) {
        if (project == null)
            return null;
        projectDao.insert(project);
        return project.getId();
    }

    @Override
    public Boolean deleteProject(Long id) {
        if (id == null)
            return false;
        projectDao.deleteById(id);
        return true;
    }

    @Override
    public Long copyProject(Long id) {
        if (id == null)
            return null;
        ProjectEntity project = projectDao.selectById(id);
        if (project == null)
            return null;
        project.setId(null);
        project.setName(project.getName() + " - 副本");
        project.setCreateTime(null);
        project.setUpdateTime(null);
        projectDao.insert(project);
        return project.getId();
    }

    @Override
    public ProjectEntity getProjectInfo(Long id) {
        if (id == null)
            return null;
        return projectDao.selectById(id);
    }
}
