package com.iamyanbing.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

/**
 */
@RestController
@Repository
@RequestMapping("/restTemplate")
public class RestTemplateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateController.class);

    //测试响应超时情况
    @PostMapping(value = "/socketTimeout")
    public String socketTimeout(@RequestParam("name") String name) {
        try {
            Thread.sleep(100000L);
        } catch (InterruptedException e) {
            LOGGER.error("socketTimeout",e);
        }
        LOGGER.info("Hello " + name);
        return "Hello " + name;
    }
}
