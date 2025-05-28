package com.xl.transactionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = false)// 不强制使用 CGLIB 代理，使用动态代理
@ComponentScan(basePackages = {"com.xl"})
@MapperScan("com.xl.transactionservice.mapper")
@EnableTransactionManagement
public class XlTransactionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XlTransactionServiceApplication.class, args);
    }

}
