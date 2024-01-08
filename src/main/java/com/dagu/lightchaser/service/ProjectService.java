package com.dagu.lightchaser.service;

import com.dagu.lightchaser.entity.ProjectEntity;

import java.util.List;

public interface ProjectService {
    Boolean updateProject(ProjectEntity project);

    List<ProjectEntity> getProjectList();

    String getProjectData(Long id);

    Long createProject(ProjectEntity project);

    Boolean deleteProject(Long id);

    Long copyProject(Long id);

    ProjectEntity getProjectInfo(Long id);

    String uploadCover(ProjectEntity project);
}
