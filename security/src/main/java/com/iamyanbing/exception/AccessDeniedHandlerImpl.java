package com.iamyanbing.exception;

import com.alibaba.fastjson.JSON;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 自定义 授权异常处理器
 * 注意： 只捕获 AccessDeniedException 异常
 * 注意： Filter 中抛出的异常，这里不捕获
 */
@Component
@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult result = ResponseResult.fail(HttpStatus.FORBIDDEN.value(), "权限不足禁止访问");
        String json = JSON.toJSONString(result);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        WebUtil.renderString(response, json);
    }
}
