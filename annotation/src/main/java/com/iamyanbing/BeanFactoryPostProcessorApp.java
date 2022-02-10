package com.iamyanbing;

import com.iamyanbing.ioc.beanfactorypostprocessor.BeanFactoryPostProcessorConfig;
import com.iamyanbing.ioc.beanfactorypostprocessor.entity.BeanFactoryPostProcessorEntity;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanFactoryPostProcessorApp {
    public static void main(String[] args) {
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(BeanFactoryPostProcessorConfig.class);

        BeanFactoryPostProcessorEntity entity = (BeanFactoryPostProcessorEntity) applicationContext.getBean("beanFactoryPostProcessorEntity");
        BeanFactoryPostProcessorEntity entity1 = (BeanFactoryPostProcessorEntity) applicationContext.getBean("beanFactoryPostProcessorEntity");

        System.out.println(entity == entity1);
    }
}
