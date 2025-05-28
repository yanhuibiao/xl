package com.xl.customercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.xl"})
@MapperScan("com.xl.customercenter.mapper")
@EnableTransactionManagement
public class XlCustomerCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(XlCustomerCenterApplication.class, args);
    }
}