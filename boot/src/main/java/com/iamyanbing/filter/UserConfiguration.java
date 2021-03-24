package com.iamyanbing.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;

/**
 * @author pengcheng
 * @date 2020/4/24 10:25
 * @description
 */
@Configuration
@ConditionalOnExpression("'${iamyanbing.auth.enable}'.equals('true')")
@Slf4j
public class UserConfiguration {

    @Bean
    public FilterRegistrationBean userFilterRegistrationBean() {
        log.info("userFilterRegistrationBean");
        //通过FilterRegistrationBean实例设置优先级可以生效
        //通过@WebFilter无效
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        //注册自定义过滤器
        filterRegistrationBean.setFilter(new UserFilter());
        //过滤器名称
        //bean.setName("flilter1");
        //过滤应用程序中所有资源,当前应用程序根下的所有文件包括多级子目录下的所有文件，注意这里*前有“/”
        filterRegistrationBean.addUrlPatterns("/*");
        //过滤指定的目录下的所有文件,当前应用程序根目录下的test下所有路径（可以是多级子路径）
        filterRegistrationBean.addUrlPatterns("/test/*");
        //过滤指定的类型文件资源, 当前应用程序根目录下的所有html文件，注意：*.html前没有“/”,否则错误
//        filterRegistrationBean.addUrlPatterns(".html");
        //过滤指定文件
//        filterRegistrationBean.addUrlPatterns("/index.html");
        //匹配路径：数组参数形式
//        filterRegistrationBean.addUrlPatterns(new String[]{"/*"});

        //优先级，越低越优先
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

}
