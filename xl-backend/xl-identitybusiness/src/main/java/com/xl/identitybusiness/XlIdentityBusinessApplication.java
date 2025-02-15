package com.xl.identitybusiness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.xl"})
public class XlIdentityBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(XlIdentityBusinessApplication.class, args);
    }

}
