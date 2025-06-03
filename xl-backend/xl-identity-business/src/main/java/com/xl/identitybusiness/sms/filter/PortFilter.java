package com.xl.identitybusiness.sms.filter;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;

//@Component
public class PortFilter implements Filter {

    @Value("${server.port}")
    private int mainPort;

    @Value("${sms.service.port}")
    private int smsPort;

    /**
     *让短信端口只处理短信的请求
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        int port = httpRequest.getServerPort();
        String path = httpRequest.getRequestURI();

        if (port == smsPort && !path.startsWith("/api/sms")) {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (port == mainPort && path.startsWith("/api/sms")) {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        chain.doFilter(request, response);
    }
}