package com.iamyanbing.controller;

import com.iamyanbing.enums.CommonStatusEnum;
import com.iamyanbing.exception.CustomException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用来访问 templates 下 .html 文件
 * <p>
 * 前后端不分离，不用
 */
@RestController
public class TestController {

    /**
     * 测试抛 CustomException 异常， 是否会被 GlobalExceptionHandler 类 customException 方法捕获
     *
     * @return
     */
    @RequestMapping("/CustomException")
    public String CustomException() {
        throw new CustomException(CommonStatusEnum.FAIL.getCode(), CommonStatusEnum.FAIL.getMessage());
    }
}
