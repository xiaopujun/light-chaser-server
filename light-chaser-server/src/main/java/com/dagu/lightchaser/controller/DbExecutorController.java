package com.dagu.lightchaser.controller;

import com.dagu.lightchaser.entity.DatasourceEntity;
import com.dagu.lightchaser.entity.DbExecutorEntity;
import com.dagu.lightchaser.global.ApiResponse;
import com.dagu.lightchaser.service.DatasourceService;
import com.dagu.lightchaser.service.DbExecutorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/db/executor")
public class DbExecutorController {

    @Resource
    private DbExecutorService dbExecutorService;

    @PostMapping("/execute")
    public ApiResponse<Object> executeSql(@RequestBody DbExecutorEntity dbExecutorEntity) {
        return ApiResponse.success(dbExecutorService.executeSql(dbExecutorEntity));
    }

}
