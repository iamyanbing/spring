package com.iamyanbing.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @RestController：提供实现了REST API，可以服务JSON,XML或者其他。这里是以String的形式渲染出结果。
 * Spring4之后加入的注解，原来在@Controller中返回json需要@ResponseBody来配合，如果直接用@RestController替代@Controller就不需要再配置@ResponseBody，默认返回json格式
 * @Auther: yanbing
 * @Date: 2019/2/22 18:08
 */
@RestController
@Repository
@RequestMapping("/hello")
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);


    /**
     * <pre>
     * 注解@RequestMapping：配置url映射。现在更多的也会直接用以Http Method直接关联的映射注解来定义，比如：GetMapping、PostMapping、DeleteMapping、PutMapping等
     *
     * 提供路由信息，"/hello"路径的HTTP Request都会被映射到sayHello方法进行处理
     *
     * value = "name":指定参数名称。源码中name的别名为value，value的别名为name ，所以两者等同
     * </pre>
     *
     * @param age
     * @param name
     * @return
     */
    @RequestMapping(value = "/{age}", method = RequestMethod.GET)
    public String sayHello(@PathVariable("age") String age, @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        LOGGER.info("sayHello, date :" + new Date());
        return "Hello " + name + "," + age;
    }

    @RequestMapping(value = "/yanbing", method = RequestMethod.GET)
    public String sayHelloYanBing() {
        LOGGER.info("Hello, yanbing, date :" + new Date());
        return "Hello yanbing";
    }

    @RequestMapping(value = "/excludePathPatterns", method = RequestMethod.GET)
    public String excludePathPatterns() {
        LOGGER.info("excludePathPatterns, date :" + new Date());
        return "excludePathPatterns";
    }

    //提供路由信息，"/hello"路径的HTTP Request都会被映射到sayHello方法进行处理
    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public String exception(@RequestParam String name) {
        LOGGER.info("exception, date :" + new Date());
        int num = 1 / 0;
        return "Hello " + name;
    }
}
