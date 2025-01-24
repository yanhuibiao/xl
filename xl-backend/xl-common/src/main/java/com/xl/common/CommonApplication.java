package com.xl.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("com.xl")
public class CommonApplication {

    public static void main(String[] args) {
//        System.setProperty("nacos.logging.default.config.enabled", "false");
        SpringApplication.run(CommonApplication.class,args);
    }


}
