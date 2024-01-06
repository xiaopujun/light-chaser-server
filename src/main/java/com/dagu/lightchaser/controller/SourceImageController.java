package com.dagu.lightchaser.controller;

import com.dagu.lightchaser.entity.SourceImageEntity;
import com.dagu.lightchaser.global.ApiResponse;
import com.dagu.lightchaser.service.SourceImageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/sourceImage")
public class SourceImageController {

    @Resource
    private SourceImageService fileService;

    @PostMapping(value = "/upload")
    public ApiResponse<String> uploadImage(SourceImageEntity sourceImageEntity) {
        return ApiResponse.success(fileService.uploadImage(sourceImageEntity));
    }

    @GetMapping("/getList/{projectId}")
    public ApiResponse<List<SourceImageEntity>> getSourceImageList(@PathVariable Long projectId) {
        return ApiResponse.success(fileService.getSourceImageList(projectId));
    }

    @GetMapping("/del/{imageId}")
    public ApiResponse<Boolean> delImageSource(@PathVariable Long imageId) {
        return ApiResponse.success(fileService.delImageSource(imageId));
    }
}
