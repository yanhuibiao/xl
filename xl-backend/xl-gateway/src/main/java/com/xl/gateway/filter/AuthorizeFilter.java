package com.xl.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl.common.enums.ResponseCodeEnum;
import com.xl.common.utils.JwtUtils;
import com.xl.common.dto.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter {

    @Autowired
    JwtUtils jwtUtils;
    /**
     *
     * @param exchange 从上下文获取数据
     * @param chain 放行下个过滤器
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //0.判断是否登录接口,是的话直接放行
        String path = exchange.getRequest().getURI().getPath();
        String regex = "/ib/auth/.*|/ib/admin/register";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        boolean isMatch = matcher.matches(); // 判断是否完全匹配
        if (isMatch) {
            return chain.filter(exchange);
        }
        // 1.获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> headers = request.getHeaders();
        // 2.获取参数值
        String token = headers.getFirst("Authorization");
        // 3.对参数判断
        if (token != null) {
            if (jwtUtils.verifyToken(token.substring(7))){
                //4.为true就放行
                return chain.filter(exchange);
            }
        }
        // 5.为false，设置状态码,拦截
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//        return response.setComplete();
//        下面是自己设置响应data
        response.getHeaders().add("Content-Type","application/json;charset=utf-8");
        DataBufferFactory bufferFactory = response.bufferFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        DataBuffer wrap = null;
        try {
            wrap = bufferFactory.wrap(objectMapper.writeValueAsBytes(ResponseEntity.errorResult(ResponseCodeEnum.TOKEN_INVALID)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        DataBuffer finalWrap = wrap;
        return response.writeWith(Mono.fromSupplier(() -> finalWrap));
    }
}
