package com.iamyanbing.interceptor;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    // 默认排除规则（静态资源、监控地址等）
    String[] defaultExcludePathPatterns = {"/actuator/**"
            , "/**/*.js", "/**/*.css"
            , "/**/*.png", "/**/*.ico", "/**/*.jpg", "/**/*.jpeg", "/**/*.gif"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //通过excludePathPatterns排除的界面不会走拦截器，可以用来配置不用登陆就可以加载的界面或访问的请求
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns();
        // 排除监控、登录、静态资源相关请求PATH
        registration.excludePathPatterns(defaultExcludePathPatterns);
        // 自定义排除请求PATH,
        if (ArrayUtils.isNotEmpty("/login/**,/hello/excludePathPatterns".split(","))) {
            registration.excludePathPatterns("/login/**,/hello/excludePathPatterns".split(","));
        }

        InterceptorRegistration authRegistration = registry.addInterceptor(new AuthenticateHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns();
        // 排除监控、登录、静态资源相关请求PATH
        authRegistration.excludePathPatterns(defaultExcludePathPatterns);
        // 自定义排除请求PATH,
        if (ArrayUtils.isNotEmpty("/login/**,/hello/excludePathPatterns".split(","))) {
            authRegistration.excludePathPatterns("/login/**,/hello/excludePathPatterns".split(","));
        }
    }
}
