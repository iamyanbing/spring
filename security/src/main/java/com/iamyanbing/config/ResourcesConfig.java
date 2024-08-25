package com.iamyanbing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * 跨域：全局配置
 **/
@Configuration
public class ResourcesConfig {

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        // 添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();

        // 设置访问 源地址
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
//        config.addAllowedOrigin("*");

        // 设置访问 源请求头
        config.addAllowedHeader(CorsConfiguration.ALL);

        // 设置访问 源请求方法
        config.addAllowedMethod(CorsConfiguration.ALL);

        // 是否发送Cookie信息
        config.setAllowCredentials(true);

        //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
//        config.addExposedHeader("*");

        // 添加映射路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        // 返回新的CorsFilter.
        return new CorsFilter(source);
    }
}
