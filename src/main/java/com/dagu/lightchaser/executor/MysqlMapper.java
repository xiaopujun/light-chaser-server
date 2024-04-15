package com.dagu.lightchaser.executor;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface MysqlMapper {

    @SelectProvider(type = CustomSQLProvider.class, method = "getCustomSQL")
    List<Map<String, Object>> executeSelect(@Param("sql") String sql);
}


