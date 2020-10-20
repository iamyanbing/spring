package com.iamyanbing;

import com.iamyanbing.annotation.imports.ImportConfig;
import com.iamyanbing.annotation.imports.entry.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ImportApp {
    public static void main(String[] args) {

        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(ImportConfig.class);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            System.out.println("beanName: " + beanName);
        }

        User user = (User) applicationContext.getBean("com.iamyanbing.annotation.imports.entry.User");
        System.out.println(user.getName());
    }
}
