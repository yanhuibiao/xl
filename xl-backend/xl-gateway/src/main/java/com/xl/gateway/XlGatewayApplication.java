package com.xl.gateway;

import com.xl.common.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
})
@ComponentScan(value = {"com.xl.common", "com.xl.gateway"},
        excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,       // 使用正则匹配包路径
        pattern = "com.xl.common.config.SecurityConfig*")
)
public class XlGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(XlGatewayApplication.class,args);
    }
}
