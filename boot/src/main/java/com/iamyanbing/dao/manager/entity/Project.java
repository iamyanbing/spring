package com.iamyanbing.dao.manager.entity;

import lombok.Data;

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

}