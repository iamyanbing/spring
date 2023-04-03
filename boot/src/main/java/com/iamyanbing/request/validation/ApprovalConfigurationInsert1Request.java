package com.iamyanbing.request.validation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author : HuangYanBing
 * @date 2022/11/10 17:11
 */
@Data
@ApiModel(description = "ApprovalConfigurationInsert1Request")
public class ApprovalConfigurationInsert1Request {

    @ApiModelProperty(value = "审批流程唯一标识", required = true)
    @NotBlank(message = "审批流程必选")
    private String processDefinitionKey;

    /**
     * 嵌套验证必须用@Valid
     */
    @ApiModelProperty(value = "审批步骤(任务)信息", required = true)
    @NotEmpty(message = "审批步骤必须要设置")
    @Valid
    List<ApprovalConfigurationInsertTask1> taskList;
}
