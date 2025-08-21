package com.dagu.lightchaser.service.impl;

import com.dagu.lightchaser.model.entity.DatasourceEntity;
import com.dagu.lightchaser.model.entity.DbExecutorEntity;
import com.dagu.lightchaser.executor.DataBaseExecuteFactory;
import com.dagu.lightchaser.global.AppException;
import com.dagu.lightchaser.service.DatasourceService;
import com.dagu.lightchaser.service.DbExecutorService;
import com.dagu.lightchaser.util.Base64Util;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class DbExecutorServiceImpl implements DbExecutorService {

    @Autowired
    private DatasourceService datasourceService;

    @Override
    public Object executeSql(DbExecutorEntity dbExecutorEntity) {
        if (dbExecutorEntity == null)
            throw new AppException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL execution is incorrect, check the SQL syntax");
        String sql = Base64Util.decode(dbExecutorEntity.getSql());
        if (!sql.trim().toLowerCase().startsWith("select"))
            throw new AppException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SQL cannot be empty and must start with select");
        DatasourceEntity dataSourceEntity = datasourceService.getDataSource(dbExecutorEntity.getId());
        if (dataSourceEntity == null)
            throw new AppException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The data source does not exist");
        JdbcTemplate jdbcTemplate = DataBaseExecuteFactory.buildDataSource(dataSourceEntity);
        try {
            List<Map<String, Object>> res = jdbcTemplate.queryForList(sql);
            if (res.size() == 1)
                return res.get(0);
            return res;
        } catch (Exception e) {
            log.error("execute sql error, please check sql syntax, sql:{}", sql, e);
            DataBaseExecuteFactory.removeDatasourceCache(dataSourceEntity);
            throw new AppException(500, "SQL execution is incorrect, check the SQL syntax");
        }
    }
}
