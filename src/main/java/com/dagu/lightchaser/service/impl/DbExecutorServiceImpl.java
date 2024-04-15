package com.dagu.lightchaser.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.dagu.lightchaser.entity.DatasourceEntity;
import com.dagu.lightchaser.entity.DbExecutorEntity;
import com.dagu.lightchaser.global.AppException;
import com.dagu.lightchaser.executor.MysqlMapper;
import com.dagu.lightchaser.service.DatasourceService;
import com.dagu.lightchaser.service.DbExecutorService;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DbExecutorServiceImpl implements DbExecutorService {

    @Resource
    private DatasourceService datasourceService;

    @Override
    public Object executeSql(DbExecutorEntity dbExecutorEntity) {
        if (dbExecutorEntity == null)
            throw new AppException(500, "sql执行错误，请检查SQL语句");
        String sql = dbExecutorEntity.getSql();
        if (sql == null || !sql.startsWith("select"))
            throw new AppException(500, "sql不能为空,且必须以select开头");

        DatasourceEntity dataSourceEntity = datasourceService.getDataSource(dbExecutorEntity.getId());
        if (dataSourceEntity == null)
            throw new AppException(500, "数据源不存在");

        // 创建数据源
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(dataSourceEntity.getUrl());
        dataSource.setUsername(dataSourceEntity.getUsername());
        dataSource.setPassword(dataSourceEntity.getPassword());

        // 创建环境
        Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(MysqlMapper.class);

        // 创建 SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        // 获取 SqlSession
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            // 执行 SQL 查询
            List<Map<String, Object>> res = sqlSession.getMapper(MysqlMapper.class).executeSelect(sql);
            sqlSession.commit();
            if (res.size() == 1)
                return res.get(0);
            return res;
        } catch (Exception e) {
            throw new AppException(500, "SQL执行错误：" + e.getMessage());
        }
    }
}
