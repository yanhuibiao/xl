package com.xl.identitybiz.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
//@EnableAspectJAutoProxy(proxyTargetClass = true)// 强制使用 CGLIB 代理
@ComponentScan(basePackages = {"com.xl"})
@MapperScan({"com.xl.identitybiz.service.mapper"})
@ServletComponentScan //扫描servlet相关的配置,druid监控页面
public class XlIdentityBizServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XlIdentityBizServiceApplication.class, args);
    }

}
