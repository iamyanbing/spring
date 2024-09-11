package com.iamyanbing.myrule;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRule {

    /**
     * 业务场景见云笔记。网约车进阶目录下《9.nacos注册中心》文章 “多数据中心服务”章节
     *
     * @param serviceInstanceListSuppliers
     * @return
     */
    @Bean
    public ReactorServiceInstanceLoadBalancer myLoadBalancerRule(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers) {
        return new MyLoadBalancerRule(serviceInstanceListSuppliers);
    }
}
