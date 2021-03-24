package com.iamyanbing.config;

import com.iamyanbing.util.CustomErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private RestTemplateBuilder builder;

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        //在使用RestTemplate进行远程接口服务调用的时候，当请求的服务出现异常：超时、服务不存在等情况的时候（响应状态非200、而是400、500HTTP状态码），就会抛出异常
        //setErrorHandler作用是当响应状态码不为200时不抛出异常，通过ResponseEntity对象中响应状态码自定义处理
        //restTemplate.setErrorHandler(new CustomErrorHandler());
        return builder
                // 设置http请求连接超时时间
                .setConnectTimeout(Duration.ofMillis(12000))
                // 设置http请求读数据超时时间
                .setReadTimeout(Duration.ofMillis(12000))
                // 自定义异常处理
                .errorHandler(new CustomErrorHandler())
                .build();
    }

}
