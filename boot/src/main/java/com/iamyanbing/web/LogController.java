package com.iamyanbing.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * log 实操
 *
 * @Author huangyanbing
 * @Date 2022/2/10 21:17
 * @description
 */
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {


    @GetMapping(value = "/all")
    public String implementAllLevels() {
        log.trace("这个级别很少用,主要是用来追踪! !");
        log.debug("经常写BUG的程序员,测试的时候,多打印日志 没毛病!");
        log.info("系统日志,没有什么问题,就是想打印日志 !");
        log.warn("这个错误很少见,不影响程序继续运行,酌情处理!");
        log.error("发生了严重的错误,程序阻断了,需要立即处理,发送警报!");
        return "success";
    }

    @GetMapping(value = "/error")
    public String implementError() {
        try {
            int a = 1 / 0;
        } catch (RuntimeException e) {
            log.error("implementError fail", e);
        }
        return "success";
    }

}
