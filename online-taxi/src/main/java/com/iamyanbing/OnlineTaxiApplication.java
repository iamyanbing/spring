package com.iamyanbing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 网约车项目（进阶版）
 *
 * @EnableAsync : 开启异步，RenewRedisLock 类使用
 */
@SpringBootApplication
@EnableAsync
public class OnlineTaxiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineTaxiApplication.class, args);
    }
}
