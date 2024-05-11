package com.dagu.lightchaser.service;

import com.dagu.lightchaser.entity.DatasourceEntity;
import com.dagu.lightchaser.entity.DbExecutorEntity;

import java.util.Map;

public interface DbExecutorService {
    Object executeSql(DbExecutorEntity dbExecutorEntity);
}
