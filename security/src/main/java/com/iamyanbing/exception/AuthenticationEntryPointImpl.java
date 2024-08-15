package com.iamyanbing.exception;

import com.alibaba.fastjson.JSON;
import com.iamyanbing.res.ResponseResult;
import com.iamyanbing.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义 认证异常处理器
 * 当用户未登录或者token解析失败时会触发这个处理器
 * <p>
 * Spring Security 框架怎么判断是否认证成功？
 * 分两种情况
 * 请求登录接口： 通过 LoginServiceImpl 类 login 方法中第一步判断用户是否认证成功。
 * 请求非登录接口： 通过 JwtAuthenticationTokenFilter 类第五步判断用户是否认证成功。
 **/
@Component
@Slf4j
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "认证失败请重新登录");
        String json = JSON.toJSONString(result);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        WebUtil.renderString(response, json);
    }
}
