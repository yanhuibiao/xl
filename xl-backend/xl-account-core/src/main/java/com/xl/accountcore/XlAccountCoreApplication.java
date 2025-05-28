package com.xl.accountcore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.xl"})
@MapperScan("com.xl.accountcore.mapper")
@EnableTransactionManagement
public class XlAccountCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(XlAccountCoreApplication.class, args);
    }
}