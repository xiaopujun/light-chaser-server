package com.dagu.lightchaser.controller;


import com.dagu.lightchaser.global.ApiResponse;
import com.dagu.lightchaser.service.DataProviderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lightchaser.core.data.provider.DataProviderInfo;
import lightchaser.core.data.provider.DataProviderSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/data-provider")
public class DataProviderController extends BaseController{



    private final DataProviderService dataProviderService;

    public DataProviderController(DataProviderService dataProviderService) {
        this.dataProviderService = dataProviderService;
    }

    @ApiOperation(value = "get supported data providers")
    @GetMapping(value = "/providers")
    public ApiResponse<List<DataProviderInfo>> listSupportedDataProviders() {
        return ApiResponse.success(dataProviderService.getSupportedDataProviders());
    }

    @ApiOperation(value = "Test data source connection")
    @PostMapping(value = "/test")
    public ApiResponse<Object> testConnection(@RequestBody DataProviderSource config) throws Exception {
        return ApiResponse.success(dataProviderService.testConnection(config));
    }

}
