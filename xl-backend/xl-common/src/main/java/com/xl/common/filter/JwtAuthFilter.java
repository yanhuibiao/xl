package com.xl.common.filter;

import com.xl.common.annotation.NotLogAnnotation;
import com.xl.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NotLogAnnotation
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    String Bearer = "Bearer ";

    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // 放行登录接口
        String regex = "/auth/.*|/admin/register|/swagger-ui.html|/swagger-ui/.*|/api-docs/.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        boolean isMatch = matcher.matches(); // 判断是否完全匹配
        if (isMatch) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从 Header 获取 Token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith(Bearer)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Token");
            return;
        }

        // 验证 Token
        token = token.substring(Bearer.length());
        if (!jwtUtils.verifyToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token void");
//            response.setContentType("application/json");
//            response.getWriter().write("{code:401, message:" + "Token void");
            return;
        }

        // 从token中获取用户名
        String username = jwtUtils.getSubject(token);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null, AuthorityUtils.createAuthorityList("1234"));
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}