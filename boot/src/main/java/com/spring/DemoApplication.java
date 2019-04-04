package com.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//应用主类 DemoApplication
//@SpringBootApplication : Spring Boot 应用的标识 。
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//SpringApplication 是 Spring Boot 应用启动类，在 main() 方法中调用 SpringApplication.run() 静态方法，即可运行一个 Spring Boot 应用。
		//SpringApplication引导应用，并将Application本身作为参数传递给run方法。具体run方法会启动嵌入式的Tomcat并初始化Spring环境及其各Spring组件
		SpringApplication.run(DemoApplication.class, args);
	}

}
