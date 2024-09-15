package com.iamyanbing.rate;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 限流的维度是什么？可以根据 ip、uri、设备号、用户id 等进行限流
 * RateComponet 设置了限流的维度是 ip
 */
@Component
public class RateComponet implements KeyResolver {

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
