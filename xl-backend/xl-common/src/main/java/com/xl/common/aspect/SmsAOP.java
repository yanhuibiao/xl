package com.xl.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl.common.annotation.SmsAnnotation;
import com.xl.common.dubbo.entity.SmsMessage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.xl.common.config.autoconfig.properties.SmsProperties.SMS_EXCHANGE;
import static com.xl.common.config.autoconfig.properties.SmsProperties.SMS_ROUTING_KEY;

@Aspect
@Component
public class SmsAOP {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ObjectMapper objectMapper;

    // 定义切点：匹配所有标注了@Loggable注解的方法
    @Pointcut("@annotation(com.xl.common.annotation.SmsAnnotation)")
    public void smsAnnotationPointcut() {}

    // 方法执行后
    @Around("smsAnnotationPointcut()")
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
        // 继续执行原方法
        Object result = pjp.proceed();
        // 获取方法签名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        // 获取注解
        SmsAnnotation annotation = method.getAnnotation(SmsAnnotation.class);
        if (annotation != null) {
            // 获取注解参数
            String msisdn = annotation.msisdn();
            String msg = annotation.msg();
            // 执行其他逻辑
            List<String> msisdns = new ArrayList<>();
            msisdns.add(msisdn);
            SmsMessage smsMessage = new SmsMessage(UUID.randomUUID().toString(), msg, LocalDateTime.now(), msisdns);
            // 将消息发送到mq
            rabbitTemplate.convertAndSend(
                    SMS_EXCHANGE,
                    SMS_ROUTING_KEY,
                    objectMapper.writeValueAsString(smsMessage));
        }
        return result;
    }
}
