package com.dagu.lightchaser.service;

import com.dagu.lightchaser.entity.DatasourceEntity;

import java.util.List;

/**
 * @author DAGU
 * @description 针对表【datasource(数据源管理)】的数据库操作Service
 * @createDate 2024-04-12 11:07:34
 */
public interface DatasourceService {

    List<DatasourceEntity> getDataSourceList();

    Long addDataSource(DatasourceEntity datasource);

    Boolean updateDataSource(DatasourceEntity datasource);

    DatasourceEntity getDataSource(Long id);

    Boolean copyDataSource(Long id);

    Boolean delDataSource(Long id);

    Boolean testDataSourceConnect(Long id);
}
