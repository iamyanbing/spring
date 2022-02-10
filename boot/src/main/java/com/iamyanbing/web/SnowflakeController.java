package com.iamyanbing.web;

import com.iamyanbing.snowflake.UtilIdHub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author huangyanbing
 * @Date 2022/2/10 21:17
 * @description
 */
@RestController
@RequestMapping("/snowflake")
@Slf4j
public class SnowflakeController {

    @Resource
    private UtilIdHub utilIdHub;

    @GetMapping("/test")
    public void test() {
        log.info(utilIdHub.nextId() + "");
    }
}
