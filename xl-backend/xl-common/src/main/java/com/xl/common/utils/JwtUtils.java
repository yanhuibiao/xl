package com.xl.common.utils;

import com.xl.common.config.autoconfig.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties){
        this.jwtProperties=jwtProperties;
    }

    /**
     * 生成的token，有效期默认30分钟
     * @param params
     * @return
     */
    public String getToken(Map params){
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(jwtProperties.getSecret())) //加密方式
                .setExpiration(new Date(currentTime + (long) jwtProperties.getExpiration() * 60 * 1000)) //过期时间戳
                .addClaims(params)
                .compact();
    }

    /**
     *
     * @param params
     * @param time_out token过期时间，单位：分钟
     * @return
     */
    public String getToken(Map params,int time_out){
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret()) //加密方式
                .setExpiration(new Date(currentTime + time_out * 60 * 1000L)) //过期时间戳
                .addClaims(params)
                .compact();
    }

    public String getToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration() * 60 * 1000L);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 获取Token中的claims信息
     */
    public String getSubject(String token) {
        return this.getClaims(token).getSubject(); // 直接获取sub字段的值
    }
    /**
     * 获取Token中的claims信息
     */
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token).getBody();
    }

    /**
     * 是否有效 true-有效，false-失效
     */
    public boolean verifyToken(String token) {
        if(StringUtils.isEmpty(token)) {
            return false;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            return false;
        }
		return true;
    }
}