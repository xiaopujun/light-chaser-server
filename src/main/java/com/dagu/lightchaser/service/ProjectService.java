package com.dagu.lightchaser.service;

import com.dagu.lightchaser.entity.ProjectEntity;

import java.util.List;

public interface ProjectService {
    Long createOrUpdateProject(ProjectEntity project);

    List<ProjectEntity> getProjectList();

    ProjectEntity getProject(Long id);
}
