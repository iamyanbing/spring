package com.iamyanbing.request.validation;

import com.iamyanbing.validation.EnumValueCheck;
import com.iamyanbing.validation.GroupCheckSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author : HuangYanBing
 * @date 2022/11/10 17:33
 */
@Data
@ApiModel(description = "ApprovalConfigurationInsertTask")
public class ApprovalConfigurationInsertTask1 {
    /**
     * @NotNull
     * approvalProcessTaskId 可以为空字符串，但是不能为null
     */
    @ApiModelProperty(value = "审批流程任务表id",required = true)
    @NotNull(message = "审批流程任务id不能为空")
    private String approvalProcessTaskId;

    @ApiModelProperty(value = "审批方式;1:任意审批,2:并行审批,3:依次审批",required = true)
    @Max(value = 3,message = "选择审批方式不对")
    @Min(value = 1,message = "选择审批方式不对")
    @NotNull(message = "审批方式必选")
    private Integer approvalType;

    @ApiModelProperty(value = "执行操作.1:同意;2:拒绝", required = true)
    @EnumValueCheck(ints = {1, 2}, message = "不支持的操作")
    @NotNull(message = "操作不能不能为空")
    private Integer exeOperation;
}
