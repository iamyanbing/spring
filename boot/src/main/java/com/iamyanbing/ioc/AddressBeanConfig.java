package com.iamyanbing.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressBeanConfig {
    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public Address createAddressBean(){
        return new Address();
    }
}
