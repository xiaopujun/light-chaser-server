package com.dagu.lightchaser.executor;

import com.alibaba.druid.pool.DruidDataSource;
import com.dagu.lightchaser.constants.DataBaseEnum;
import com.dagu.lightchaser.entity.DatasourceEntity;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.util.HashMap;
import java.util.Map;

public class DataBaseExecuteFactory {

    private static final Map<DataBaseEnum, String> databaseDriverMap = new HashMap<>();

    private static final Map<String, SqlSessionFactory> datasourceCache = new HashMap<>();

    static {
        databaseDriverMap.put(DataBaseEnum.MySQL, "com.mysql.jdbc.Driver");
        databaseDriverMap.put(DataBaseEnum.ORACLE, "oracle.jdbc.driver.OracleDriver");
        databaseDriverMap.put(DataBaseEnum.SQLServer, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        databaseDriverMap.put(DataBaseEnum.PostgresSQL, "org.postgresql.Driver");
    }

    public static SqlSessionFactory buildDataSource(DatasourceEntity datasourceEntity) {
        String key = datasourceEntity.getUrl() + "_" + datasourceEntity.getUsername() + "_" + datasourceEntity.getPassword();
        if (datasourceCache.containsKey(key))
            return datasourceCache.get(key);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(databaseDriverMap.get(datasourceEntity.getType()));
        dataSource.setUrl(datasourceEntity.getUrl());
        dataSource.setUsername(datasourceEntity.getUsername());
        dataSource.setPassword(datasourceEntity.getPassword());
        Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(DataBaselMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        datasourceCache.put(key, sqlSessionFactory);
        return sqlSessionFactory;
    }
}
