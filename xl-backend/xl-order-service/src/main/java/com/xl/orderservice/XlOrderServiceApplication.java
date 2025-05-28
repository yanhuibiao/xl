package com.xl.orderservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.xl"})
@MapperScan("com.xl.orderservice.mapper")
@EnableTransactionManagement
public class XlOrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(XlOrderServiceApplication.class, args);
    }
}