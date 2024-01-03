package com.dagu.lightchaser.controller;

import com.dagu.lightchaser.entity.ImageEntity;
import com.dagu.lightchaser.global.ApiResponse;
import com.dagu.lightchaser.service.FileService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping(value = "/image/upload")
    public ApiResponse<String> uploadImage(ImageEntity imageEntity) {
        return ApiResponse.success(fileService.uploadImage(imageEntity));
    }

    @GetMapping("/getImageList/{projectId}")
    public ApiResponse<List<ImageEntity>> getImageSourceList(@PathVariable String projectId) {
        return ApiResponse.success(fileService.getImageSourceList(projectId));
    }
}
