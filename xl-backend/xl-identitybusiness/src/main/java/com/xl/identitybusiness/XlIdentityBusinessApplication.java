package com.xl.identitybusiness;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication()
@EnableAspectJAutoProxy(proxyTargetClass = true)// 强制使用 CGLIB 代理
@ComponentScan(basePackages = {"com.xl"})
@MapperScan("com.xl.identitybusiness.mapper")
public class XlIdentityBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(XlIdentityBusinessApplication.class, args);
    }

}
