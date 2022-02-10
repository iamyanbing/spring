package com.iamyanbing.interceptor;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangyanbing
 * @date 2019-11-05 19:04
 */
public class AuthenticateHandlerInterceptor extends HandlerInterceptorAdapter {

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
//        if (!uri.startsWith("/page/") || uri.equalsIgnoreCase("/page/index.html") || uri.equalsIgnoreCase("/page/main.html")) {
//            return true;
//        }


        //只校验?前面部分，后面部分不需要校验
        int index = uri.indexOf("?");
        if (index > -1) {
            uri = uri.substring(0, uri.indexOf("?"));
        }

        //permissions用来配置登陆之后就有的权限
        String[] permissions = {"/restTemplate/**","/hello/yanbing", "/hello/**","/HttpServlet/**","/users/getFilterUser",
                "/swagger-ui.html", "/snowflake/**"};

        //匹配排除 全路径 情况
        for (String permission : permissions) {
            if (StringUtils.isNotBlank(permission) && (permission.equals(uri) || permission.equals(uri.substring(1)))) {
                return true;
            }
        }

        //匹配排除 /** 情况
        if (permissions != null) {
            for (String permission : permissions) {
                if (antPathMatcher.match(permission, uri)) {
                    return true;
                }
            }
        }

        this.doUnauthorizedResponse(response);
        return false;
    }

    /**
     * 响应未登录状态信息
     *
     * @param response
     * @throws IOException
     */
    private void doUnauthorizedResponse(HttpServletResponse response) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("code", 403);  // 指定401表示未登录（目前前端可能无法处理HTTP自身的401状态码）
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(data));
        response.getWriter().flush();
    }
}
