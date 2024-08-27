package com.iamyanbing.exception;

import com.iamyanbing.res.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 注意： Filter 中抛出的异常，这里不捕获
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseResult customException(CustomException e) {
        log.error("请求参数校验异常. 异常信息:{}", e.getMessage());
        return ResponseResult.fail(e.getCode(), e.getLocalizedMessage());
    }

    /**
     * 认证异常处理
     * <p>
     * 这里的HTTP协议响应码可以是200，也可以是401，具体是哪个值？ 根据公司项目来
     */
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseResult authException(AuthException e) {
        log.error("认证异常. 异常信息:{}", e.getMessage());
        return ResponseResult.fail(e.getCode(), e.getLocalizedMessage());
    }
}
