package com.dagu.lightchaser.service.impl;

import com.dagu.lightchaser.entity.DatasourceEntity;
import com.dagu.lightchaser.entity.DbExecutorEntity;
import com.dagu.lightchaser.executor.DataBaseExecuteFactory;
import com.dagu.lightchaser.executor.DataBaselMapper;
import com.dagu.lightchaser.global.AppException;
import com.dagu.lightchaser.service.DatasourceService;
import com.dagu.lightchaser.service.DbExecutorService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DbExecutorServiceImpl implements DbExecutorService {

    private static final Logger logger = LoggerFactory.getLogger(DbExecutorServiceImpl.class);

    @Resource
    private DatasourceService datasourceService;

    @Override
    public Object executeSql(DbExecutorEntity dbExecutorEntity) {
        if (dbExecutorEntity == null)
            throw new AppException(500, "sql执行错误，请检查SQL语句");
        String sql = dbExecutorEntity.getSql();
        if (sql == null || !sql.trim().startsWith("select"))
            throw new AppException(500, "sql不能为空,且必须以select开头");
        DatasourceEntity dataSourceEntity = datasourceService.getDataSource(dbExecutorEntity.getId());
        if (dataSourceEntity == null)
            throw new AppException(500, "数据源不存在");

        SqlSessionFactory sqlSessionFactory = DataBaseExecuteFactory.buildDataSource(dataSourceEntity);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<Map<String, Object>> res = sqlSession.getMapper(DataBaselMapper.class).executeQuery(sql);
            logger.info("SQL执行成功,SQL: {}, 结果为: {}", sql, res);
            if (res.size() == 1)
                return res.get(0);
            return res;
        } catch (Exception e) {
            logger.error("SQL执行错误, 请检查SQL语法", e);
            throw new AppException(500, "SQL执行错误, 请检查SQL语法");
        }
    }
}
