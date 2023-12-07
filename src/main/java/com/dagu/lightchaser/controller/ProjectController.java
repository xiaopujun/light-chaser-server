package com.dagu.lightchaser.controller;

import com.dagu.lightchaser.entity.ProjectEntity;
import com.dagu.lightchaser.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Resource
    private ProjectService projectService;

    @GetMapping("/list")
    public List<ProjectEntity> getProjectList() {
        return projectService.getProjectList();
    }

    @GetMapping("/getProjectData/{id}")
    public String getProjectData(@PathVariable(name = "id") Long id) {
        return projectService.getProjectData(id);
    }

    @GetMapping("/getProjectInfo/{id}")
    public ProjectEntity getProjectInfo(@PathVariable(name = "id") Long id) {
        return projectService.getProjectInfo(id);
    }

    @PostMapping("/update")
    public Boolean updateProject(@RequestBody(required = true) ProjectEntity project) {
        return projectService.updateProject(project);
    }

    @PostMapping("/create")
    public Long createProject(@RequestBody(required = true) ProjectEntity project) {
        return projectService.createProject(project);
    }

    @GetMapping("/del/{id}")
    public Boolean deleteProject(@PathVariable(name = "id") Long id) {
        return projectService.deleteProject(id);
    }

    @GetMapping("/copy/{id}")
    public Long copyProject(@PathVariable(name = "id") Long id) {
        return projectService.copyProject(id);
    }
}
