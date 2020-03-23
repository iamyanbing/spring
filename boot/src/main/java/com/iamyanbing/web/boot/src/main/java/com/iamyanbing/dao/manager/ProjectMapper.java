package com.iamyanbing.web;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

/**
 * @Auther: yanbing
 * @Date: 2019/2/22 18:08
 */
@RestController
@RequestMapping("/HttpServlet")
public class HttpServletRequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServletRequestController.class);


    /**
     * <pre>
     * HttpServletRequest getParameter相关的方法可以获取的参数
     * 1：get请求参数
     * 2：post请求，请求类型为：Content-Type = application/x-www-form-urlencoded
     *
     * req.getInputStream()不能获取get请求参数，也不能获取post请求Content-Type = application/x-www-form-urlencoded 参数
     * </pre>
     *
     * @return
     */
    @RequestMapping(value = "/getParams")
    public String getParams(HttpServletRequest req) {
        String method = req.getMethod();
        LOGGER.info("req.getMethod() : {}", method);
        LOGGER.info("req.getLocalPort() : {}", req.getLocalPort());
        LOGGER.info("req.getRemotePort() : {}", req.getRemotePort());
        LOGGER.info("req.getServerPort() : {}", req.getServerPort());
        String name = req.getParameter("name");
        LOGGER.info("req.getParameter() : {}", name);
        Map<String, String[]> parameterMap = req.getParameterMap();
        LOGGER.info("req.getParameterMap() : {}", new Gson().toJson(parameterMap));
        Enumeration<String> enumeration = req.getParameterNames();
        while(enumeration.hasMoreElements()){
            String value = (String)enumeration.nextElement();//调用nextElement方法获得元素
            LOGGER.info("enumeration.nextElement() : {}", value);
        }
        LOGGER.info("req.getParameterNames() : {}", new Gson().toJson(enumeration));
        String[] parameterValues = req.getParameterValues("name");
        LOGGER.info("req.getParameterValues() : {}", new Gson().toJson(parameterValues));

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
            StringBuilder body = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            LOGGER.info("req.getInputStream()：{}", body.toString());
        } catch (IOException e) {
            LOGGER.error("getParams接口执行异常",e);
        }
        return "success";
    }

    /**
     * req.getInputStream()可以获取POST请求  Content-Type = application/json类型参数
     * @param req
     * @return
     */
    @PostMapping(value = "/getParamsFromJson")
    public String getParamsFromJson(HttpServletRequest req) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
            StringBuilder body = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            LOGGER.info("req.getInputStream()：{}", body.toString());
        } catch (IOException e) {
            LOGGER.error("getParams接口执行异常",e);
        }
        return "success";
    }

}
