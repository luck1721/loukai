package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication()
@EnableJpaRepositories(basePackages = "com.example.demo.bll.dao")
@ComponentScan(basePackages={"com.example.demo.bll","com.example.demo.web"})
@MapperScan("com.example.demo.bll.mapper")
@ImportResource("classpath*:springApplication-*.xml")
@EnableCaching
@EnableRetry
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
