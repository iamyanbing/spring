package com.iamyanbing.dao.manager.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Project {
    private Integer id;

    private Integer projectId;

    private String name;

    private String code;

    private Boolean isDeleted;

    private BigDecimal totalHours;

    private BigDecimal totalEstimate;

    private BigDecimal totalConsumed;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    /**
     * 是否启用分页（默认启用）
     */
    private Boolean pageFlag = true;

    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 是否正序排序
     */
    private Boolean asc = true;

    /**
     * 当前记录起始索引
     */
    private Integer pageIndex;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;

}