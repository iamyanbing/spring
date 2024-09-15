package com.iamyanbing.exception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class GatewayExceptionConfig {

    /**
     * @Order(Ordered.HIGHEST_PRECEDENCE)
     * 提高 MyExceptionHandler 对象的优先级，即先执行 MyExceptionHandler。
     * 只有提高了 MyExceptionHandler 对象的优先级，MyExceptionHandler 对象才能生效。
     * <p>
     * Ordered.HIGHEST_PRECEDENCE : 代表优先级最高
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public MyExceptionHandler myExceptionHandler() {
        return new MyExceptionHandler();
    }
}
