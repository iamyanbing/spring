package com.spring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Auther: yanbing
 * @Date: 2019/2/22 18:08
 */
//@RestController：提供实现了REST API，可以服务JSON,XML或者其他。这里是以String的形式渲染出结果。
@RestController
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    //提供路由信息，"/hello"路径的HTTP Request都会被映射到sayHello方法进行处理
    @RequestMapping("/hello")
    public String sayHello() {
        LOGGER.info("date :" + new Date());
        return "Hello World";
    }
}
