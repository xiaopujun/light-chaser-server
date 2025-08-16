package com.dagu.lightchaser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dagu.lightchaser.config.CryptoConfig;
import com.dagu.lightchaser.constants.DataBaseEnum;
import com.dagu.lightchaser.dto.DatasourceAddRequest;
import com.dagu.lightchaser.dto.DatasourceUpdateRequest;
import com.dagu.lightchaser.entity.DatasourceEntity;
import com.dagu.lightchaser.entity.PageParamEntity;
import com.dagu.lightchaser.executor.DataBaseExecuteFactory;
import com.dagu.lightchaser.global.AppException;
import com.dagu.lightchaser.mapper.DatasourceMapper;
import com.dagu.lightchaser.service.DatasourceService;
import com.dagu.lightchaser.util.CryptoUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author DAGU
 * @description 针对表【datasource(数据源管理)】的数据库操作Service实现
 * @createDate 2024-04-12 11:07:34
 */
@Service
public class DatasourceServiceImpl implements DatasourceService {

    private static final Logger logger = LoggerFactory.getLogger(DatasourceServiceImpl.class);

    @Autowired
    private DatasourceMapper datasourceMapper;

    @Autowired
    private CryptoConfig cryptoConfig;


    @Override
    public List<DatasourceEntity> getDataSourceList() {
        LambdaQueryWrapper<DatasourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DatasourceEntity::getId, DatasourceEntity::getName, DatasourceEntity::getUsername, DatasourceEntity::getType, DatasourceEntity::getUrl);
        return datasourceMapper.selectList(wrapper);
    }

    @Override
    public Long addDataSource(DatasourceEntity datasource) {
        if (datasource == null)
            return null;
        datasource.setCreateTime(LocalDateTime.now());
        datasourceMapper.insert(datasource);
        return datasource.getId();
    }

    @Override
    public Long addDataSource(DatasourceAddRequest request) {
        try {
            String encryptedPassword = getEncryptedPassword(request.getPassword(), request.getAesKey());

            // 5. 创建DatasourceEntity并保存
            DatasourceEntity datasource = new DatasourceEntity();
            datasource.setName(request.getName());
            datasource.setType(request.getType());
            datasource.setUsername(request.getUsername());
            datasource.setPassword(encryptedPassword);
            datasource.setUrl(request.getUrl());
            datasource.setCreateTime(LocalDateTime.now());

            datasourceMapper.insert(datasource);
            return datasource.getId();
        } catch (Exception e) {
            logger.error("Failed to add datasource with encryption: ", e);
            throw new AppException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "数据源添加失败: " + e.getMessage());
        }
    }

    private String getEncryptedPassword(String password,String aesKey) throws Exception {
        // 1. 从配置文件读取RSA私钥（CryptoConfig已经处理过格式）
        String privateKeyString = cryptoConfig.getRsa().getPrivateKey();

        // 2. 使用RSA私钥解密password（password是RSA加密后的AES加密密码）
        String encryptedPasswordByAES = CryptoUtil.decryptByRSAPrivateKey(password, privateKeyString);

        // 3. 使用前端AES密钥解密password（支持IV:密文格式，aesKey是明文）
        String realPassword = CryptoUtil.decryptByAESWithIV(encryptedPasswordByAES, aesKey);

        // 4. 使用后端AES密钥加密password
        return CryptoUtil.encryptByAES(realPassword, cryptoConfig.getAes().getKey());
    }

    @Override
    public Boolean updateDataSource(DatasourceUpdateRequest datasource) throws Exception {
        if (datasource.getId() == null)
            return false;

        String encryptedPassword = getEncryptedPassword(datasource.getPassword(), datasource.getAesKey());

        return datasourceMapper.update(new LambdaUpdateWrapper<DatasourceEntity>()
                .eq(DatasourceEntity::getId, datasource.getId())
                .set(DatasourceEntity::getName, datasource.getName())
                .set(DatasourceEntity::getUsername, datasource.getUsername())
                .set(DatasourceEntity::getPassword, encryptedPassword)
                .set(DatasourceEntity::getType, datasource.getType())
                .set(DatasourceEntity::getUrl, datasource.getUrl())
                .set(DatasourceEntity::getUpdateTime, LocalDateTime.now())
        ) > 0;
    }

    @Override
    public DatasourceEntity getDataSource(Long id) {
        if (id == null)
            return null;
        LambdaQueryWrapper<DatasourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DatasourceEntity::getId, DatasourceEntity::getName, DatasourceEntity::getUsername, DatasourceEntity::getPassword, DatasourceEntity::getType, DatasourceEntity::getUrl);
        wrapper.eq(DatasourceEntity::getId, id);
        return datasourceMapper.selectOne(wrapper);
    }

    @Override
    public Boolean copyDataSource(Long id) {
        if (id == null)
            return false;
        DatasourceEntity datasource = getDataSource(id);
        if (datasource == null)
            return false;
        datasource.setId(null);
        datasource.setCreateTime(null);
        datasource.setUpdateTime(null);
        datasource.setName(datasource.getName() + "（副本）");
        return addDataSource(datasource) > 0;
    }

    @Override
    public Boolean delDataSource(Long id) {
        if (id == null)
            return false;
        return datasourceMapper.deleteById(id) > 0;
    }

    /**
     * 测试数据源连接
     *
     * @param id 数据源ID
     * @return {@link Boolean} 连接结果
     */
    @Override
    public Boolean testDataSourceConnect(Long id) {
        if (id == null) {
            logger.warn("数据源ID为空，无法进行连接测试");
            return false;
        }

        DatasourceEntity datasource = getDataSource(id);
        if (datasource == null) {
            logger.warn("未找到ID为{}的数据源", id);
            return false;
        }

        try {
            logger.info("开始测试数据源连接: {} ({})", datasource.getName(), datasource.getType());

            datasource.setPassword(CryptoUtil.decryptByAESWithIV2(datasource.getPassword(), cryptoConfig.getAes().getKey()));
            // 构建数据源连接（已包含连接测试）
            JdbcTemplate jdbcTemplate = DataBaseExecuteFactory.buildDataSource(datasource);

            // 执行特定的测试查询
            String testSql = getTestSqlByType(datasource.getType());
            jdbcTemplate.queryForObject(testSql, Integer.class);

            logger.info("数据源连接测试成功: {} ({})", datasource.getName(), datasource.getType());
            return true;

        } catch (Exception exception) {
            logger.error("数据源连接测试失败: {} ({}), 错误信息: {}",
                    datasource.getName(), datasource.getType(), exception.getMessage(), exception);

            // 移除缓存的失败连接
            DataBaseExecuteFactory.removeDatasourceCache(datasource);

            // 抛出友好的错误信息
            String errorMessage = buildUserFriendlyErrorMessage(datasource.getType(), exception);
            throw new AppException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    /**
     * 根据数据库类型获取测试SQL
     *
     * @param dbType 数据库类型
     * @return 测试SQL语句
     */
    private String getTestSqlByType(DataBaseEnum dbType) {
        switch (dbType) {
            case ORACLE:
                return "SELECT 1 FROM DUAL";
            case SQLServer:
                return "SELECT 1";
            case MySQL:
            case PostgresSQL:
            case SQLite:
            default:
                return "SELECT 1";
        }
    }

    /**
     * 构建用户友好的错误信息
     *
     * @param dbType    数据库类型
     * @param exception 异常信息
     * @return 友好的错误信息
     */
    private String buildUserFriendlyErrorMessage(DataBaseEnum dbType, Exception exception) {
        String message = exception.getMessage();
        String dbTypeName = dbType.getName();

        // 常见错误的友好提示
        if (message.contains("Connection refused") || message.contains("No route to host")) {
            return String.format("%s数据库连接被拒绝，请检查数据库服务是否启动以及网络连接", dbTypeName);
        } else if (message.contains("Unknown database") || message.contains("database") && message.contains("does not exist")) {
            return String.format("%s数据库不存在，请检查数据库名称是否正确", dbTypeName);
        } else if (message.contains("Access denied") || message.contains("authentication failed")) {
            return String.format("%s数据库认证失败，请检查用户名和密码", dbTypeName);
        } else if (message.contains("timeout")) {
            return String.format("%s数据库连接超时，请检查网络连接或数据库响应时间", dbTypeName);
        } else if (message.contains("No suitable driver found")) {
            return String.format("未找到%s数据库驱动，请检查JDBC驱动是否正确", dbTypeName);
        } else {
            return String.format("%s数据库连接失败: %s", dbTypeName, message);
        }
    }

    @Override
    public Page<DatasourceEntity> getDataSourcePageList(PageParamEntity pageParam) {
        if (pageParam == null)
            return new Page<>();
        Page<DatasourceEntity> page = new Page<>();
        page.setSize(pageParam.getSize() == 0 ? 10 : pageParam.getSize());
        page.setCurrent(pageParam.getCurrent() == 0 ? 1 : pageParam.getCurrent());
        LambdaQueryWrapper<DatasourceEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DatasourceEntity::getId, DatasourceEntity::getName, DatasourceEntity::getUsername, DatasourceEntity::getType, DatasourceEntity::getUrl);
        if (pageParam.getSearchValue() != null && !pageParam.getSearchValue().isEmpty())
            wrapper.like(DatasourceEntity::getName, pageParam.getSearchValue());
        return datasourceMapper.selectPage(page, wrapper);
    }

}




