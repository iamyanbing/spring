package com.iamyanbing.snowflake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(UtilIdHub.class)
@EnableConfigurationProperties(UtilIdHubProperties.class)
public class UtilIdHubAutoConfigure {
    private final UtilIdHubProperties properties;

    @Autowired
    public UtilIdHubAutoConfigure(UtilIdHubProperties properties) {
        this.properties = properties;
    }

    @Bean
    UtilIdHub utilHub() {
        return new UtilIdHub(properties.getWorkerId(), properties.getDatacenterId());
    }
}
