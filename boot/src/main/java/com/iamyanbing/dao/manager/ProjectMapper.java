package com.iamyanbing.dao.manager;

import com.iamyanbing.dao.manager.entity.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Project record);

    int insertBatch(List<Project> record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Integer id);

    /**
     * xml中只能用list，不能用types
     *
     * @param types
     * @return
     */
    List<Project> selectByTypes(List<String> types);

    /**
     * xml中可以用types
     *
     * @param types
     * @return
     */
    List<Project> selectByTypesParam(@Param("types") List<String> types);

    /**
     * xml中可以用types
     *
     * @param types
     * @return
     */
    List<Project> selectByTypesAndNameParam(@Param("types") List<String> types, @Param("name") String name);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);

    int updateBatch(List<Project> record);


    /**
     * 指定分页规则和排序规则
     * @param record
     * @return
     */
    List<Project> selectByEntity(Project record);
}
