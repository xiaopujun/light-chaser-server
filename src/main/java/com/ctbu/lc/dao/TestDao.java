package com.ctbu.lc.dao;

import com.ctbu.lc.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestDao {

    long updateData(@Param("id") long id, @Param("name") String name);

    Long insertData(@Param("testEntity") TestEntity testEntity);
}
