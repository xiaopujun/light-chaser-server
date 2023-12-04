package com.dagu.lightchaser.dao;

import com.dagu.lightchaser.entity.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectDao {
    /**
     * 添加项目
     *
     * @param project 项目实体
     * @return 项目id
     */
    Long addProject(@Param("project") ProjectEntity project);

    /**
     * 删除项目
     *
     * @param project 项目实体
     * @return 是否更新成功
     */
    Boolean updateProject(@Param("project") ProjectEntity project);

    /**
     * 查询所有项目
     *
     * @return 项目列表
     */
    List<ProjectEntity> getProjectList();

    /**
     * 查询项目
     *
     * @param id 项目id
     * @return 项目实体
     */
    ProjectEntity getProject(@Param("id") Long id);
}
