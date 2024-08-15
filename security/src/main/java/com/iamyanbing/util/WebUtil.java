package com.iamyanbing.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class WebUtil {

    /**
     * 将字符串渲染到客户端
     *
     * @param response
     * @param string   待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            if (response.getStatus() == 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            }
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
            response.getWriter().flush();
        } catch (IOException e) {
            log.error("响应结果给客户端失败.", e);
        }
        return null;
    }
}
