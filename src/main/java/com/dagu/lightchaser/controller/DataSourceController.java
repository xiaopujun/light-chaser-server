package com.dagu.lightchaser.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.dagu.lightchaser.entity.DatasourceEntity;
import com.dagu.lightchaser.entity.PageParamEntity;
import com.dagu.lightchaser.global.ApiResponse;
import com.dagu.lightchaser.service.DatasourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/datasource")
public class DataSourceController {

    @Resource
    private DatasourceService datasourceService;

    @GetMapping("/get/{id}")
    public ApiResponse<DatasourceEntity> getDataSource(@PathVariable Long id) {
        return ApiResponse.success(datasourceService.getDataSource(id));
    }

    @GetMapping("/list")
    public ApiResponse<List<DatasourceEntity>> getDataSourceList() {
        return ApiResponse.success(datasourceService.getDataSourceList());
    }

    @PostMapping("/pageList")
    public ApiResponse<Page<DatasourceEntity>> getDataSourcePageList(@RequestBody PageParamEntity pageParam) {
        return ApiResponse.success(datasourceService.getDataSourcePageList(pageParam));
    }

    @PostMapping("/add")
    public ApiResponse<Long> addDataSource(@RequestBody DatasourceEntity datasource) {
        return ApiResponse.success(datasourceService.addDataSource(datasource));
    }

    @PostMapping("/update")
    public ApiResponse<Boolean> updateDataSource(@RequestBody DatasourceEntity datasource) {
        return ApiResponse.success(datasourceService.updateDataSource(datasource));
    }

    @GetMapping("/copy/{id}")
    public ApiResponse<Boolean> copyDataSource(@PathVariable Long id) {
        return ApiResponse.success(datasourceService.copyDataSource(id));
    }

    @GetMapping("/del/{id}")
    public ApiResponse<Boolean> delDataSource(@PathVariable Long id) {
        return ApiResponse.success(datasourceService.delDataSource(id));
    }

    @GetMapping("/connect/{id}")
    public ApiResponse<Boolean> testDataSourceConnect(@PathVariable Long id) {
        return ApiResponse.success("链接正常", datasourceService.testDataSourceConnect(id));
    }

}
