package com.iamyanbing.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    //value = "name":指定参数名称。源码中name的别名为value，value的别名为name ，所以两者等同
    @RequestMapping(value = "/{age}/hello", method = RequestMethod.GET)
    public String sayHello(@PathVariable("age") String age, @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        LOGGER.info("sayHello, date :" + new Date());
        return "Hello " + name + "," + age;
    }

    //提供路由信息，"/hello"路径的HTTP Request都会被映射到sayHello方法进行处理
    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public String exception(@RequestParam String name) {
        LOGGER.info("exception, date :" + new Date());
        int num = 1 / 0;
        return "Hello " + name;
    }
}
