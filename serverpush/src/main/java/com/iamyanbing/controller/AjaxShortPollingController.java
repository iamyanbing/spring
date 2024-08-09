package com.iamyanbing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *Ajax短轮询实现类
 * 这个类的注解，不要再加@RestController，加完这个类所有的接口返回都自动成了json格式了，我们这里用@Controller就好
 */
@Controller
public class AjaxShortPollingController {

    private static Logger logger = LoggerFactory.getLogger(AjaxShortPollingController.class);

    // shortPolling是浏览器访问JSP路径
    // return 返回值中的 AjaxShortPolling 表示 AjaxShortPolling.jsp页面。
    @RequestMapping("/shortPolling")
    public String shortPolling(){
        logger.info("shortPolling success");
        return "AjaxShortPolling";
    }

    @RequestMapping(value="/ajaxShortPolling",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ajaxShortPolling(){
        logger.info("ajaxShortPolling success");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }


    //主要作用：测试项目搭建是否成功
    @RequestMapping("/hello")
    public String hello(){
        logger.info("index success");
        return "Hello";
    }
}
