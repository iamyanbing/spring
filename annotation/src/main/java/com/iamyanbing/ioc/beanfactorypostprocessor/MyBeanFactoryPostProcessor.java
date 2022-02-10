package com.iamyanbing.ioc.beanfactorypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Author huangyanbing
 * @Date 2021/7/16 10:07
 * @description
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNameS = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNameS) {
            if ("beanFactoryPostProcessorEntity".equalsIgnoreCase(beanDefinitionName)) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
                beanDefinition.setScope("prototype");
            }
        }
    }
}
