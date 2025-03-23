package com.xl.common.exception;

import com.xl.common.dto.ResponseEntity;
import com.xl.common.enums.ResponseCodeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.common.utils.JsonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 没有权限访问时的异常处理类
 */
public class SpringSecurityAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        ResponseEntity<?> responseEntity = ResponseEntity.errorResult(ResponseCodeEnum.NO_PERMISSION);
        PrintWriter out = response.getWriter();
        out.println(JsonUtils.toJson(responseEntity));
        out.flush(); //释放内存
    }
}
