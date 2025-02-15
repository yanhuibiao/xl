package com.xl.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.xl"})
public class XlTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(XlTransactionApplication.class, args);
    }

}
