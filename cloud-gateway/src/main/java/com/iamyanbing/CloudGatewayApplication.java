package com.iamyanbing;

import com.iamyanbing.config.TagLoadBalancerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

/**
 * 网约车项目（进阶版）
 * 网关
 * <p>
 * LoadBalancerClients 启动类上配置，网关对所有请求的转发都会使用 TagLoadBalanceronfig
 * application.yml 中  uri 格式必须是：lb://service-name。只有这样配置，LoadBalancerClients才生效。
 */
@SpringBootApplication
@LoadBalancerClients(defaultConfiguration = {TagLoadBalancerConfig.class})
public class CloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudGatewayApplication.class, args);
    }
}
