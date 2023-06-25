package com.iamyanbing.web;

import com.iamyanbing.request.validation.ApprovalConfigurationInsert1Request;
import com.iamyanbing.request.validation.ApprovalConfigurationInsertRequest;
import com.iamyanbing.snowflake.UtilIdHub;
import com.iamyanbing.validation.GroupCheckSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.groups.Default;

/**
 * @Author huangyanbing
 * @Date 2022/2/10 21:17
 * @description
 */
@RestController
@RequestMapping("/validation")
@Slf4j
public class ValidationController {

    @Resource
    private UtilIdHub utilIdHub;

    /**
     * 对象校验，必须在对象前面加 @Validated
     * @param req
     * @return
     */
    @PostMapping("/insert")
    public Long insert(@RequestBody @Validated({GroupCheckSequence.class, Default.class}) ApprovalConfigurationInsertRequest req) {
        return utilIdHub.nextId();
    }

    /**
     * 对象校验，必须在对象前面加 @Validated
     * 不需要顺序校验，则直接是@Validated(),而不是@Validated(GroupCheckSequence.class)
     * 对象中属性不能加groups = {GroupCheckSequence.GroupA.class}，不然不能校验
     * @param req
     * @return
     */
    @PostMapping("/insert1")
    public Long insert1(@RequestBody @Validated() ApprovalConfigurationInsert1Request req) {
        return utilIdHub.nextId();
    }
}
