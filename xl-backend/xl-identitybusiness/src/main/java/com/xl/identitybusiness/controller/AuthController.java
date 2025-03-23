package com.xl.identitybusiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xl.common.annotation.LogAnnotation;
import com.xl.common.dto.LoginDto;
import com.xl.common.dubbo.api.CustomerService;
import com.xl.common.dubbo.entity.Customer;
import com.xl.common.enums.ResponseCodeEnum;
import com.xl.common.exception.BusinessException;
import com.xl.common.utils.JwtUtils;
import com.xl.common.dto.ResponseEntity;
import com.xl.common.utils.Generator;
import com.xl.identitybusiness.service.impl.AdministratorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final int CAPTCHA_LENGTH = 4;  // 默认验证码长度
    private static final int CAPTCHA_REQUEST_LIMIT = 3; // 每分钟最多请求次数
    private static final int CAPTCHA_REQUEST_INTERVAL = 10; // 请求验证码的最小间隔时间（秒）
    private static final int CAPTCHA_EXPIRY_TIME = 1; // 验证码有效期（分钟）

    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate<Object, Object>  redisTemplate;

    @Autowired
    CustomerService customerService;

    @Autowired
    AdministratorServiceImpl administratorService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request,@RequestBody LoginDto loginDto) {
        // 验证码校验
        String var1 = (String) redisTemplate.opsForValue().get("captcha"+":"+loginDto.getCaptcha());
        if ("".equals(var1) || var1 == null){
            return ResponseEntity.errorResult(55,"Verification code error");
        }
        // 验证码session验证
        String captchaSession = request.getHeader("captcha-session");
        String var2 =(String) redisTemplate.opsForValue().get("captcha-session"+":"+loginDto.getCaptcha());
        if ( "".equals(var2) || var2 == null || !var2.equals(captchaSession)) {
            return ResponseEntity.errorResult(55,"Verification code error");
        }
        // 验证码验证成功删除验证码缓存
        redisTemplate.delete("captcha::" + loginDto.getCaptcha());
        redisTemplate.delete("captcha-session::" + loginDto.getCaptcha());
        // 定义成立成功返回的token
        String token;
        try {
            token = administratorService.login(loginDto.getUsername(), loginDto.getPassword());
        }catch (Exception e){
            return ResponseEntity.errorResult(ResponseCodeEnum.LOGIN_FAILED);
        }
        if (token == null) {
            return ResponseEntity.errorResult(ResponseCodeEnum.LOGIN_FAILED);
        }
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseEntity.okResult(map);
    }



    @GetMapping("/getcaptcha")
    @Operation(summary = "获取验证码", description = "获取验证码")
    @LogAnnotation
    public ResponseEntity getCaptcha(@RequestHeader Map header, HttpSession session, @RequestParam Map param, HttpServletResponse response) throws IOException {
        System.out.println("header:"+header+"\n"+"session:"+session +"\n"+"param:"+param +"\n"+"response:"+response);
        String a = UUID.randomUUID().toString();
        String[] temp = Generator.generateCaptchaBase64Image();
        response.setHeader("captcha-session",a);
        redisTemplate.opsForValue().set("captcha-session"+":"+temp[0],a, 1, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("captcha"+":"+temp[0],temp[1], 1, TimeUnit.MINUTES);
        Map<String,String> map = new HashMap<>();
        map.put("code",temp[0]);
        map.put("codeBase64",temp[1]);
        return ResponseEntity.okResult(map);
    }

    @PostMapping("/login1")
    public ResponseEntity login(@RequestBody LoginDto loginDto, @RequestHeader Map header){
        // 验证码session验证
        String var1 =(String) redisTemplate.opsForValue().get("captcha-session"+"::"+loginDto.getCaptcha());
        if (!header.containsKey("captcha-session") || "".equals(var1) || var1 == null){
            return ResponseEntity.errorResult(55,"Verification code error");
        }else if (!(header.get("captcha-session").equals(var1))){
            return ResponseEntity.errorResult(55,"Verification code error");
        }
        // 验证码校验
        String var2 = (String) redisTemplate.opsForValue().get("captcha"+"::"+loginDto.getCaptcha());
        if ("".equals(var2) || var2 == null){
            return ResponseEntity.errorResult(55,"Verification code error");
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
        tokenValueMap.put("id",customer.getIdentityId());
        tokenValueMap.put("username",customer.getUsername());
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",jwtUtils.getToken(tokenValueMap));
        // 将 token 存储到 Redis
        redisTemplate.opsForValue().set("token::" + customer.getUsername(), tokenMap.get("token"));
        return ResponseEntity.okResult(tokenMap);
    }

    // 生成并返回验证码图像及ID
    @GetMapping("/captcha")
    public ResponseEntity<?> getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取请求者的 IP 地址
        String clientIp = getClientIp(request);
        // 定义验证码在Redis中储存的key
        String requestCountKey = "captcha_request_count:" + clientIp;
        String lastRequestTimeKey = "captcha_last_request_time:" + clientIp;
        // 获取ip请求次数
        Integer requestCountInit = (Integer) redisTemplate.opsForValue().get(requestCountKey);
        Integer requestCount = requestCountInit == null ? 0 : requestCountInit;
        // 限制每个IP地址请求次数
        if (requestCount >= CAPTCHA_REQUEST_LIMIT) {
            return ResponseEntity.errorResult(ResponseCodeEnum.BAD_REQUEST,
                    "Too many requests. Please wait and try again later.");
        }
        // 获取ip上次请求时间
        String lastRequestTimeStr = (String) redisTemplate.opsForValue().get(lastRequestTimeKey);
        long currentTime = System.currentTimeMillis();
        // 限制每个IP地址请求频率（在10秒内只能请求一次验证码）,如果上次请求时间存在，则判断是否超过10秒
        if (lastRequestTimeStr != null) {
            long lastRequestTime = Long.parseLong(lastRequestTimeStr);
            long timeDifference = (currentTime - lastRequestTime) / 1000; // 秒
            if (timeDifference < CAPTCHA_REQUEST_INTERVAL) {
                return ResponseEntity.errorResult(ResponseCodeEnum.BAD_REQUEST,
                        "The request time is too short. Please wait and try again later.");
            }
        }
        // 生成验证码文本和ID
        String captchaText = generateCaptchaText();
        String a = UUID.randomUUID().toString();
        // 存储验证码和ID在 Redis
        redisTemplate.opsForValue().set("captcha_text:" + captchaText, a, CAPTCHA_EXPIRY_TIME, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("captcha-session"+":"+captchaText,a, CAPTCHA_EXPIRY_TIME, TimeUnit.MINUTES);
        // 存储请求次数和最后请求时间
        redisTemplate.opsForValue().increment(requestCountKey, 1);
        redisTemplate.expire(requestCountKey, 1, TimeUnit.MINUTES);  // 每分钟自动清空请求次数
        redisTemplate.opsForValue().set(lastRequestTimeKey, String.valueOf(currentTime), CAPTCHA_REQUEST_INTERVAL, TimeUnit.SECONDS);
        // 设置响应头为图片
        response.setContentType("image/jpeg");
        response.setHeader("captcha-session",a);
        // 生成验证码图片并写入响应流
        BufferedImage image = generateCaptchaImage(captchaText);
        ImageIO.write(image, "JPEG", response.getOutputStream());

        // 返回验证码ID（在前端登录时与用户输入的验证码进行验证）
        return ResponseEntity.okResult(new HashMap<>().put("captchaId:", a));
    }

    // 验证用户输入的验证码
    private boolean validateCaptcha(String captchaSession, String captcha) {
        String userInputCaptcha = (String) redisTemplate.opsForValue().get("captcha_text:" + captcha);
        if (userInputCaptcha == null) {
            return false;
        }
        String tempSession = (String) redisTemplate.opsForValue().get("captcha-session:" + captcha);
        if (tempSession == null) {
            return false;
        }else if (captchaSession.equals(tempSession)) {
            return true;
        }
        return false;
    }


    // 生成验证码
    private String generateCaptchaText() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            captcha.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return captcha.toString();
    }

    // 生成验证码图片
    private BufferedImage generateCaptchaImage(String captchaText) {
        int width = 160, height = 60;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString(captchaText, 30, 40);  // 绘制验证码
        g.dispose();
        return image;
    }

    // 获取请求者的 IP 地址
    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

}
