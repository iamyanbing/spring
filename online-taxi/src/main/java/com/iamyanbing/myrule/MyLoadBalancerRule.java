package com.iamyanbing.myrule;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;

public class MyLoadBalancerRule implements ReactorServiceInstanceLoadBalancer {


    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers;

    public MyLoadBalancerRule(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers) {
        this.serviceInstanceListSuppliers = serviceInstanceListSuppliers;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {

        ServiceInstanceListSupplier ifAvailable = serviceInstanceListSuppliers.getIfAvailable();

        return ifAvailable.get().next().map(this::getInstanceResponse);
    }

    @Autowired
    NacosDiscoveryProperties nacosDiscoveryProperties;

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {

        // 拿到我当前服务的 cluster-name
        String myClusterName = nacosDiscoveryProperties.getClusterName();

        ServiceInstance serviceInstance = null;

        for (int i = 0; i < instances.size(); i++) {
            serviceInstance = instances.get(i);
            String instanceClusterName = serviceInstance.getMetadata().get("nacos.cluster");
            // 如果匹配上了，则循环结束，返回匹配上的 serviceInstance
            // 如果匹配不上，则返回遍历的最后一个 serviceInstance
            if (myClusterName.equals(instanceClusterName)) {
                break;
            }
        }
        return new DefaultResponse(serviceInstance);
    }

}
