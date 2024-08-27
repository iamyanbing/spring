package com.iamyanbing.controller;

import com.iamyanbing.common.Constants;
import com.iamyanbing.enums.CommonStatusEnum;
import com.iamyanbing.exception.AuthException;
import com.iamyanbing.exception.CustomException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class ExceptionController {


    /**
     * 接受 JwtAuthenticationTokenFilter 抛出的异常，然后抛给 GlobalExceptionHandler 处理
     *
     * @return
     */
    @RequestMapping(Constants.AUTH_EXCRPTION_PATH)
    public void handleAuthException(HttpServletRequest request) {
        throw (AuthException) request.getAttribute("authException");
    }

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
