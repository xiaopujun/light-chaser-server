package com.dagu.lightchaser.controller;

import com.dagu.lightchaser.entity.ProjectEntity;
import com.dagu.lightchaser.global.ApiResponse;
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
    public ApiResponse<List<ProjectEntity>> getProjectList() {
        return ApiResponse.success(projectService.getProjectList());
    }

    @GetMapping("/getProjectData/{id}")
    public ApiResponse<String> getProjectData(@PathVariable(name = "id") Long id) {
        return ApiResponse.success(projectService.getProjectData(id));
    }

    @GetMapping("/getProjectInfo/{id}")
    public ApiResponse<ProjectEntity> getProjectInfo(@PathVariable(name = "id") Long id) {
        return ApiResponse.success(projectService.getProjectInfo(id));
    }

    @PostMapping("/update")
    public ApiResponse<Boolean> updateProject(@RequestBody ProjectEntity project) {
        return ApiResponse.success(projectService.updateProject(project));
    }

    @PostMapping("/create")
    public ApiResponse<Long> createProject(@RequestBody ProjectEntity project) {
        return ApiResponse.success(projectService.createProject(project));
    }

    @GetMapping("/del/{id}")
    public ApiResponse<Boolean> deleteProject(@PathVariable(name = "id") Long id) {
        return ApiResponse.success(projectService.deleteProject(id));
    }

    @GetMapping("/copy/{id}")
    public ApiResponse<Long> copyProject(@PathVariable(name = "id") Long id) {
        return ApiResponse.success(projectService.copyProject(id));
    }
}
