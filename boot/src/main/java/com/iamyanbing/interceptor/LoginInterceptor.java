package com.iamyanbing.interceptor;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String ip = getIpAddress(request);
        LOGGER.info("ip: " + ip); //request.getRemoteAddr();
        Map<String, String> map = getParameterMap(request);// 获取url中的所有参数
        if (map != null){
            for (Map.Entry<String,String> entry:
            map.entrySet()) {
                LOGGER.info("Param key: " + entry.getKey() + "; value :" + entry.getValue());
            }
        }
        String servletUrl = request.getServletPath();// servlet地址
        String url = getRealUrl(servletUrl, map);
        LOGGER.info("request.getScheme() : "+ request.getScheme());
        LOGGER.info("request.getRequestURI() : "+ request.getRequestURI());
        LOGGER.info("request.getRequestURL() : "+ request.getRequestURL());
        LOGGER.info("request.getServerName() : "+ request.getServerName());
        LOGGER.info("request.getServletContext().getRealPath(\"/\") : "+ request.getServletContext().getRealPath("/"));
        LOGGER.info(url);
        LOGGER.info("request.getServletPath() : "+ request.getServletPath());
        LOGGER.info("request.getAuthType() : "+ request.getAuthType());
        LOGGER.info("request.getContextPath() : "+ request.getContextPath());
        LOGGER.info("request.getMethod() : "+ request.getMethod());
        LOGGER.info("request.getPathInfo() : "+ request.getPathInfo());
        LOGGER.info("request.getPathTranslated() : "+ request.getPathTranslated());
        LOGGER.info("request.getLocalAddr() : "+ request.getLocalAddr());
        LOGGER.info("request.getLocalName() : "+ request.getLocalName());
        LOGGER.info("request.getRemoteAddr() : "+ request.getRemoteAddr());
        LOGGER.info("request.getRemoteUser() : "+ request.getRemoteUser());
        LOGGER.info("request.getRemoteHost() : "+ request.getRemoteHost());
        LOGGER.info("request.getRemotePort() : "+ request.getRemotePort());
        LOGGER.info("request.getLocalPort() : "+ request.getLocalPort());
        LOGGER.info("request.getServerPort() : "+ request.getServerPort());
        LOGGER.info("request.getProtocol() : "+ request.getProtocol());
        return true;
    }

    /**
     * 获取url
     *
     * @param uri
     * @param params
     * @return
     */
    String getRealUrl(String uri, Map<String, String> params) {
        StringBuffer sb = new StringBuffer(uri);
        if (params != null) {
            int i = 0;
            for (String key : params.keySet()) {
                i++;
                if (i == 1) {
                    sb.append("?" + key + "=" + params.get(key));
                } else {
                    sb.append("&" + key + "=" + params.get(key));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 根据request获取所有的参数集
     *
     * @param request
     * @return
     */
    protected Map<String, String> getParameterMap(HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();
        String name;
        Map<String, String> map = new HashMap();
        while (names.hasMoreElements()) {
            name = names.nextElement();
            map.put(name, request.getParameter(name).trim());
        }
        return map;
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request)
            throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                // if (logger.isInfoEnabled()) {
                // logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip="
                // + ip);
                // }
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                // if (logger.isInfoEnabled()) {
                // logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip="
                // + ip);
                // }
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                // if (logger.isInfoEnabled()) {
                // logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip="
                // + ip);
                // }
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                // if (logger.isInfoEnabled()) {
                // logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip="
                // + ip);
                // }
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                // if (logger.isInfoEnabled()) {
                // logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip="
                // + ip);
                // }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}

