package com.dagu.lightchaser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dagu.lightchaser.constants.DataBaseEnum;
import com.dagu.lightchaser.entity.DatasourceEntity;
import com.dagu.lightchaser.entity.PageParamEntity;
import com.dagu.lightchaser.executor.DataBaseExecuteFactory;
import com.dagu.lightchaser.global.AppException;
import com.dagu.lightchaser.mapper.DatasourceMapper;
import com.dagu.lightchaser.service.DatasourceService;
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
    public Boolean updateDataSource(DatasourceEntity datasource) {
        if (datasource.getId() == null)
            return false;
        datasource.setUpdateTime(LocalDateTime.now());
        return datasourceMapper.updateById(datasource) > 0;
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

    @Override
    public Boolean testDataSourceConnect(Long id) {
        if (id == null)
            return false;
        DatasourceEntity datasource = getDataSource(id);
        if (datasource == null)
            return false;
        try {
            JdbcTemplate jdbcTemplate = DataBaseExecuteFactory.buildDataSource(datasource);
            if (DataBaseEnum.ORACLE.equals(datasource.getType())) {
                jdbcTemplate.execute("select 1 from dual");
            } else
                jdbcTemplate.execute("select 1");
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
            DataBaseExecuteFactory.removeDatasourceCache(datasource);
            throw new AppException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "link failed: " + exception.getMessage());
        }
        return true;
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




