package com.xl.identitybusiness.controller;

import com.xl.common.annotation.LogAnnotation;
import com.xl.common.dto.ResponseResult;
import com.xl.common.utils.Generator;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class Authentication {

    @GetMapping("/getcaptcha")
    @Operation(summary = "获取验证码", description = "获取验证码")
    @LogAnnotation
    public ResponseResult getCaptcha() throws IOException {
        String[] temp = Generator.generateCaptchaBase64Image();
//        redisTemplate.opsForValue().set("captcha"+"::"+temp[0],temp[1], captchaTimeout, TimeUnit.MINUTES);
        Map<String,String> map = new HashMap<>();
        map.put("code",temp[0]);
        map.put("codeBase64",temp[1]);
        return ResponseResult.okResult(map);
    }
}
