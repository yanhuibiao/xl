package com.xl.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl.common.entity.ResponseEntity;
import com.xl.common.enums.ResponseCodeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.common.utils.JsonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 没有权限访问时的异常处理类
 */
public class SpringSecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 1. 设置响应状态码为 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 2. 自定义响应内容（如 JSON 格式）
        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", "Token void");
        // 3. 序列化并写入响应
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
