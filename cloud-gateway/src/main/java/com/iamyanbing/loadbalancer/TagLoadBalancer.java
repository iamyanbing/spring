package com.iamyanbing.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TagLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    String serviceId;

    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListProviders;

    final String TAG = "tag";
    final String VERSION = "version";

    public TagLoadBalancer(String serviceId, ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListProviders) {
        this.serviceId = serviceId;
        this.serviceInstanceListProviders = serviceInstanceListProviders;
    }


    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {

        ServiceInstanceListSupplier ifAvailable = serviceInstanceListProviders.getIfAvailable(NoopServiceInstanceListSupplier::new);


        return ifAvailable.get(request).next().map(serviceInstances -> this.getInstanceResponse(request, ifAvailable, serviceInstances));
    }

    /**
     * LoadBalancerClients 启动类上配置，网关对所有请求的转发都会使用 TagLoadBalanceronfig.
     * 即网关对所有请求的转发都会调用本方法
     *
     * @param request
     * @param supplier
     * @param serviceInstances
     * @return
     */
    private Response<ServiceInstance> getInstanceResponse(Request request, ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances) {

        ServiceInstance resultServiceInstance = null;
        // 从请求中取东西
        List<String> tags = ((RequestDataContext) (request.getContext())).getClientRequest().getHeaders().get(TAG);
        if (tags == null) {
            // 请求头没有 tag，也就是其他不需要灰度发布的服务。负载均衡实现
            // 参考的是 RoundRobinLoadBalancer 类 getInstanceResponse() 代码
            // RoundRobinLoadBalancer 是 Spring Cloud Gateway 默认的负载均衡
            // RoundRobinLoadBalancer 类 choose() -> processInstanceResponse() -> getInstanceResponse()
            int seedPosition = new Random().nextInt(1000);
            AtomicInteger position = new AtomicInteger(seedPosition);

            // Spring Cloud Gateway 老版本代码
            int pos = Math.abs(position.incrementAndGet());

            // Spring Cloud Gateway 新版本代码
            // int pos = this.position.incrementAndGet() & Integer.MAX_VALUE;

            ServiceInstance instance = serviceInstances.get(pos % serviceInstances.size());

            resultServiceInstance = instance;
        } else {
            // 请求头有 tag，灰度发布。负载均衡实现
            String tag = tags.get(0);
            for (int i = 0; i < serviceInstances.size(); i++) {
                ServiceInstance serviceInstance = serviceInstances.get(i);
                String version = serviceInstance.getMetadata().get(VERSION);
                if ((tag.equals("num") && version.equals("v1")) || (tag.equals("str") && version.equals("v2"))) {
                    resultServiceInstance = serviceInstance;
                    break;
                }
            }
        }


        return new DefaultResponse((resultServiceInstance));

    }
}
