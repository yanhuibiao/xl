package com.xl.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 应用端点的安全配置（优先级高）
    @Bean
    @Order(1)  // 数值越小优先级越高
    public SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
        System.out.printf("appFilterChain start");
        http
                .securityMatcher("/api1/**")  // 仅匹配应用端点
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        return http.build();
    }

    // Actuator 端点的安全配置（优先级低）
    @Bean
    @Order(2)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/actuator/**")  // 匹配 Actuator 端点
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())  // 允许匿名访问
                .csrf().disable();  // 禁用 CSRF（按需配置）
        return http.build();
    }
}