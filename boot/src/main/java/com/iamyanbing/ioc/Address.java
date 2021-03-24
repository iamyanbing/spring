package com.iamyanbing.ioc;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 关于在spring  容器初始化 bean 和销毁前所做的操作定义方式有三种
 *
 * 第一种注解：
 * 　　通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作
 *
 * 第二种是XML配置：
 * 　　通过 在xml中定义init-method 和  destory-method方法
 *
 * 第三种是接口实现：
 * 　　通过bean实现InitializingBean和 DisposableBean接口
 */
public class Address implements InitializingBean , DisposableBean {
    private String province;
    private String city;
    private String town;

    @PostConstruct
    public void PostConstruct(){
        System.out.println("@PostConstruct");
    }

    @PreDestroy
    public void PreDestroy(){
        System.out.println("@PreDestroy");
    }

    public void initMethod(){
        System.out.println("对象被初始化 : initMethod");
    }

    public void destroyMethod(){
        System.out.println("对象被销毁 : destroyMethod");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean : afterPropertiesSet");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean : destroy");
    }

    public Address() {
        System.out.println("address被创建了");
    }

    public Address(String province, String city, String town) {
        this.province = province;
        this.city = city;
        this.town = town;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }



}
