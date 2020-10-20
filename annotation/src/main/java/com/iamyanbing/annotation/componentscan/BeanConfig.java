package com.iamyanbing.annotation.componentscan;

import org.springframework.context.annotation.ComponentScan;

/**
 * @ComponentScan 默认会把同路径下资源也加载进IOC
 * @ComponentScan(value = "com.iamyanbing.annotation.controller") 只扫描指定包下资源进IOC
 */
@ComponentScan
//@ComponentScan(value = "com.iamyanbing.annotation.controller")
public class BeanConfig {
}
