package com.iamyanbing.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iamyanbing.util.WebUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 自定义认证失败处理器
 **/
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("msg", "登录失败" + exception.getMessage());
        map.put("status", 500);
        String str = new ObjectMapper().writeValueAsString(map);
        WebUtil.renderString(response, str);
    }
}
