package com.xl.identitybiz.service.controller;

import com.alibaba.nacos.client.config.impl.ConfigHttpClientManager;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xl.common.dto.ResponseEntity;
import com.xl.common.dubbo.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customerRest")
public class CustomerRestTemplateController {

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

//    通过restTemplate调用customer服务的controller，这里会重复创建token，仅作为demo代码使用
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Customer customer) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        // 1. 将token设置到请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());  // 设置Bearer Token
        // 2. 创建请求实体
//        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);
        // 避免@TableField("security_credential")导致password丢失
        Map customerMap = objectToMap(customer);
        customerMap.put("password", customer.getPassword());
        HttpEntity<Map> requestEntity = new HttpEntity<>(customerMap, headers);
        // 3. 发送 POST 请求
        org.springframework.http.ResponseEntity<ResponseEntity> exchange = restTemplate.exchange("http://localhost:18084/cust/customer/register", HttpMethod.POST, requestEntity, ResponseEntity.class);
        // 4. 处理响应
        if (exchange.getStatusCode() == HttpStatus.OK) {
            ResponseEntity responseBody = exchange.getBody();
            return responseBody;
        } else {
            return ResponseEntity.errorResult(exchange.getStatusCode().value(),"注册失败");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getCustomer(@PathVariable String username) throws Exception {
        // 1. 创建 RestTemplate 实例，准备请求头
        RestTemplate restTemplate = new RestTemplate();
        // 1. 将token设置到请求头
        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity forObject = restTemplate.getForObject(URI.create("http://localhost:18084/cust/customer/" + username), ResponseEntity.class);
//        org.springframework.http.ResponseEntity<ResponseEntity> forEntity = restTemplate.getForEntity("http://localhost:18084/cust/customer/" + username, ResponseEntity.class, headers);
        org.springframework.http.ResponseEntity<ResponseEntity> forEntity = restTemplate.exchange("http://localhost:18084/cust/customer/" + username, HttpMethod.GET, entity, ResponseEntity.class);
        objectMapper.registerModule(new JavaTimeModule());  // 解决异常：com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
        Customer customer = objectMapper.readValue(objectMapper.writeValueAsString(forEntity.getBody().getData()) ,Customer.class);
        return ResponseEntity.okResult(customer);
    }

    public String getToken() throws Exception {
        // 1. 创建 RestTemplate 实例，准备请求头
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // 2. 获取验证码
//        ResponseEntity captchaObject = restTemplate.getForObject(URI.create("http://localhost:18081/ib/auth/getcaptcha"), ResponseEntity.class);
        org.springframework.http.ResponseEntity<ResponseEntity> captchaObject = restTemplate.getForEntity(URI.create("http://localhost:18081/ib/auth/getcaptcha"), ResponseEntity.class);
        // 设置验证码的session
        headers.set("captcha-session",captchaObject.getHeaders().get("captcha-session").get(0));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String code = objectToMap(captchaObject.getBody().getData()).get("code").toString();
        // 3. 登录获取token
        Map<String,String> loginData = new HashMap<>();
        loginData.put("captcha",code);
        loginData.put("username","admin");
        loginData.put("password","1234");
//        ResponseEntity  loginObject = restTemplate.postForObject(URI.create("http://localhost:18081/ib/auth/login"), loginData, ResponseEntity.class);
        // 5. 将请求头和请求体封装到HttpEntity中
        HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(loginData, headers);
        org.springframework.http.ResponseEntity<ResponseEntity> loginObject = restTemplate.postForEntity(URI.create("http://localhost:18081/ib/auth/login"), requestEntity, ResponseEntity.class);
        // objectMapper.readValue()方法使用JsonParser
        String jsonString = objectMapper.writeValueAsString(loginObject.getBody().getData());
        if (jsonString == "\"\""){return "";}
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(jsonString);
        Map token = objectMapper.readValue(parser, Map.class);
        return token.get("token").toString();
    }

    // 将JSON字符串转换为Map
    public Map<String, Object> jsonToMap(String json) throws Exception {
        return objectMapper.readValue(json, Map.class);
    }

    // 将Java对象转换为Map（先将对象转为JSON字符串，再转为Map）
    public Map<String, Object> objectToMap(Object object) throws Exception {
        String json = objectMapper.writeValueAsString(object);
        return objectMapper.readValue(json, Map.class);
    }
}
