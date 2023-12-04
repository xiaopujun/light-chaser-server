package com.dagu.lightchaser.service.impl;

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
    public Long createOrUpdateProject(ProjectEntity project) {
        if (project == null)
            return null;
        if (project.getId() == null) {
            projectDao.addProject(project);
            return project.getId();
        } else {
            project.setUpdateTime(LocalDateTime.now());
            projectDao.updateProject(project);
            return project.getId();
        }
    }

    @Override
    public List<ProjectEntity> getProjectList() {
        return projectDao.getProjectList();
    }

    @Override
    public ProjectEntity getProject(Long id) {
        if (id == null)
            return null;
        return projectDao.getProject(id);
    }
}
