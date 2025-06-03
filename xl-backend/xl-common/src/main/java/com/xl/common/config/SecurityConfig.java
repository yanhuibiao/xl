package com.xl.common.config;

import com.xl.common.exception.SpringSecurityAuthenticationEntryPoint;
import com.xl.common.filter.JwtAuthFilter;
import com.xl.common.exception.SpringSecurityAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用 @PreAuthorize 注解
public class SecurityConfig {

    JwtAuthFilter JwtAuthFilter;

    public SecurityConfig(JwtAuthFilter JwtAuthFilter) {
        this.JwtAuthFilter = JwtAuthFilter;
    }

    SpringSecurityAccessDeniedHandler springSecurityAccessDeniedHandler = new SpringSecurityAccessDeniedHandler();
    SpringSecurityAuthenticationEntryPoint springSecurityAuthenticationEntryPoint = new SpringSecurityAuthenticationEntryPoint();

    // 应用端点的安全配置（优先级高）
    @Bean
    @Order(1)  // 数值越小优先级越高
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // 禁用csrf
                // .formLogin().disable()  // 关闭默认登录
                // .securityMatcher("/api/**")  // 仅匹配应用端点
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()   // 禁止session
                .exceptionHandling().authenticationEntryPoint(springSecurityAuthenticationEntryPoint) // 处理未认证异常
                .accessDeniedHandler(springSecurityAccessDeniedHandler)  // 处理权限不足异常
                .and().authorizeHttpRequests().requestMatchers(HttpMethod.OPTIONS).permitAll();  //运行跨域请求
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**", "/actuator",
                                "/actuator/**","/admin/register","/auth/**","/ws/**").permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/druid/**")).permitAll().anyRequest().authenticated());
// Spring Security 6.x会尝试判断该路径是Spring MVC端点还是普通Servlet端点。由于Druid的监控页面通过独立的StatViewServlet注册（映射到 /druid/*），
// 而Spring MVC的DispatcherServlet通常映射到根路径/，此时Spring Security无法自动确定应使用MvcRequestMatcher还是AntPathRequestMatcher，导致匹配规则失效
// requestMatchers("/**").hasRole("ADMIN") 要求需要有admin权限
        http.addFilterBefore(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Actuator 端点的安全配置（优先级低）
//    @Bean
//    @Order(2)
//    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/actuator/**")  // 匹配 Actuator 端点
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())  // 允许匿名访问
//                .csrf().disable();  // 禁用 CSRF（按需配置）
//        return http.build();
//    }


    @Bean
    @Lazy   //有循环依赖，可以尝试使用@Lazy注解来延迟某些Bean的初始化。这将强制Spring延迟创建某些Bean，避免在启动时立即初始化所有Bean。
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}