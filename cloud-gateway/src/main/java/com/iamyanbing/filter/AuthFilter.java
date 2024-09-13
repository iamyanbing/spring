package com.iamyanbing.filter;

import com.iamyanbing.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 在网关添加了认证之后，后面所有服务都不需要再添加认证
 * <p>
 * 通过 Ordered 设置 AuthFilter 类的优先级，即让 AuthFilter 优先一些框架自带的过滤器（包括局部过滤器和全局过滤器）
 * eg：如果不设置 Ordered，则 yml 配置文件 StripPrefix 过滤器会优先执行，则 AuthFilter 获取的请求路径就会和实际不符。
 * <p>
 * 注意：这里用  @Order 不生效
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    List<String> authPaths;

    @Autowired
    PathInterceptorConfig pathInterceptorConfig;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long start = System.currentTimeMillis();
        authPaths = pathInterceptorConfig.getExcludePathPatterns();
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getPath().toString();
        log.info("path: {}", path);
        String pathContainer = request.getPath().pathWithinApplication().value();
        log.info("pathContainer: {}", pathContainer);
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        log.info("remoteAddress: {}", remoteAddress.toString());
        log.info("remoteAddress getAddress: {}", remoteAddress.getAddress());
        log.info("remoteAddress getPort: {}", remoteAddress.getPort());
        log.info("remoteAddress getHostName {}", remoteAddress.getHostName());
        log.info("remoteAddress getHostString {}", remoteAddress.getHostString());
        boolean flag = true;
        String result = "";
        boolean authFlag = true;


        if (authPaths != null) {
            for (String authPath : authPaths) {
                //匹配排除 全路径 情况
                if (StringUtils.isNotBlank(authPath) && !authPath.contains("**") && (authPath.equals(path) || authPath.equals(path.substring(1)))) {
                    authFlag = false;
                    break;
                }

                //匹配排除 /** 情况
                if (authPath.contains("**") && antPathMatcher.match(authPath, path)) {
                    authFlag = false;
                    break;
                }
            }
        }

        if (authFlag) {
            log.info("需要校验auth");
            String token = request.getHeaders().getFirst("Authorization");

            // 解析token
            String userId = JwtUtils.parseToken(token);

            if (userId == null) {
                result = "access token invalid";
                flag = false;
            } else {

                // 这里直接赋值
                // 企业开发是从redis中取出token
                String tokenRedis = token;
                if ((StringUtils.isBlank(tokenRedis)) || (!token.trim().equals(tokenRedis.trim()))) {
                    result = "access token invalid";
                    flag = false;
                }
            }

        } else {
            log.info("不需要校验auth");
        }

        // 判断标识
        if (flag) {
            return chain.filter(exchange)
                    //filter的后置处理
                    .then(Mono.fromRunnable(() -> {
                        ServerHttpResponse response = exchange.getResponse();
                        HttpStatus statusCode = response.getStatusCode();
                        long end = System.currentTimeMillis();
                        long interval = end - start;
                        log.info("请求路径: {},远程IP地址: {},响应码: {}, 请求时长:{}", path, remoteAddress, statusCode, interval);
                    }));
        } else {
            // 校验不通过
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // ServerHttpResponse 设置响应体见 MyExceptionHandler 类

            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
