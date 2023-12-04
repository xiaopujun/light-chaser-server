package com.dagu.lightchaser.controller;

import com.dagu.lightchaser.entity.ProjectEntity;
import com.dagu.lightchaser.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Resource
    private ProjectService projectService;

    @GetMapping("/list")
    public List<ProjectEntity> getProjectList() {
        return projectService.getProjectList();
    }

    @GetMapping("/get")
    public ProjectEntity getProject(@RequestParam(name = "id") Long id) {
        return projectService.getProject(id);
    }

    @PostMapping("/createOrUpdate")
    public Long createOrUpdateProject(@RequestBody(required = true) ProjectEntity project) {
        return projectService.createOrUpdateProject(project);
    }
}
