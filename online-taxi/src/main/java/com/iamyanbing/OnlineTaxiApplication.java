package com.iamyanbing;

import com.iamyanbing.myrule.MyRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 网约车项目（进阶版）
 *
 * service-verificationcode: 验证码服务
 *
 * @EnableAsync : 开启异步，RenewRedisLock 类使用
 */
@SpringBootApplication
@EnableAsync
@LoadBalancerClient(name = "service-verificationcode",configuration = MyRule.class)
public class OnlineTaxiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineTaxiApplication.class, args);
    }
}
