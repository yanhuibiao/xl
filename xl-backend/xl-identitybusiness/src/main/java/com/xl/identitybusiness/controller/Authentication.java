package com.xl.identitybusiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xl.common.annotation.LogAnnotation;
import com.xl.common.dto.LoginDto;
import com.xl.common.dubbo.api.CustomerService;
import com.xl.common.dubbo.dao.Customer;
import com.xl.common.enums.ResponseCodeEnum;
import com.xl.common.exception.BusinessException;
import com.xl.common.utils.JwtUtils;
import com.xl.common.dto.ResponseResult;
import com.xl.common.utils.Generator;
import com.xl.common.utils.JwtUtils;
import com.xl.identitybusiness.mapper.CustomerMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class Authentication {

    @Autowired
    private RedisTemplate<Object, Object>  redisTemplate;

    @Autowired
    CustomerService customerService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/getcaptcha")
    @Operation(summary = "获取验证码", description = "获取验证码")
    @LogAnnotation
    public ResponseResult getCaptcha(@RequestHeader Map header, HttpSession session, @RequestParam Map param, HttpServletResponse response) throws IOException {
        System.out.println(header);
        System.out.println(session);
        String a = UUID.randomUUID().toString();
//        System.out.println(a);
//        session.setAttribute("verificationCode", a);
//        session.setAttribute("verificationCodeExpireTime", System.currentTimeMillis() + 300000);
        System.out.println(param);
        System.out.println(response);
//        response.setHeader("Verification-Token",a);
        String[] temp = Generator.generateCaptchaBase64Image();
        response.setHeader("captcha-session",a);
        redisTemplate.opsForValue().set("captcha-session"+"::"+temp[0],a, 3, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("captcha"+"::"+temp[0],temp[1], 30, TimeUnit.MINUTES);
        Map<String,String> map = new HashMap<>();
        map.put("code",temp[0]);
        map.put("codeBase64",temp[1]);
        return ResponseResult.okResult(map);
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginDto loginDto,@RequestHeader Map header){
        // 验证码session验证
        String var1 =(String) redisTemplate.opsForValue().get("captcha-session"+"::"+loginDto.getCaptcha());
        if (!header.containsKey("captcha-session") || "".equals(var1) || var1 == null){
            return ResponseResult.errorResult(55,"Verification code error");
        }else if (!(header.get("captcha-session").equals(var1))){
            return ResponseResult.errorResult(55,"Verification code error");
        }
        // 验证码校验
        String var2 = (String) redisTemplate.opsForValue().get("captcha"+"::"+loginDto.getCaptcha());
        if ("".equals(var2) || var2 == null){
            return ResponseResult.errorResult(55,"Verification code error");
        }
        // 验证码验证成功删除验证码缓存
        redisTemplate.delete("captcha::" + loginDto.getCaptcha());
        // 校验密码，失败抛出登录异常
        LambdaQueryWrapper<Customer> qw =new LambdaQueryWrapper<Customer>();
        qw.eq(Customer::getUsername,loginDto.getUsername());
        qw.eq(Customer::getPassword,loginDto.getPassword());
        Customer customer =  customerService.getOne(qw);
        if (customer == null){
            throw new BusinessException(ResponseCodeEnum.LOGIN_FAILED);
        }
        // 验证密码成功
        Map<String,String> tokenValueMap = new HashMap<>();
        tokenValueMap.put("id",customer.getId());
        tokenValueMap.put("username",customer.getUsername());
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",jwtUtils.getToken(tokenValueMap));
        // 将 token 存储到 Redis
        redisTemplate.opsForValue().set("token::" + customer.getUsername(), tokenMap.get("token"));
        return ResponseResult.okResult(tokenMap);
    }
}
