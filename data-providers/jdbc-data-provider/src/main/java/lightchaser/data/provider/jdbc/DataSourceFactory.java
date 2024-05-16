package lightchaser.data.provider.jdbc;

import com.dagu.lightchaser.dataprovider.base.provider.jdbc.JdbcProperties;

import javax.sql.DataSource;

public interface DataSourceFactory<T extends DataSource> {

    T createDataSource(JdbcProperties jdbcProperties) throws Exception;

    void destroy(DataSource dataSource);

}