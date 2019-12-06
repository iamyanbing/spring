package com.iamyanbing.dao.manager;

import com.iamyanbing.dao.manager.entity.Project;

import java.util.List;

public interface ProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Project record);

    int insertBatch(List<Project> record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);

    int updateBatch(Project record);
}