package com.xl.identitybiz.smsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl.common.dubbo.api.SmsServiceTool;
import com.xl.common.dubbo.entity.SmsMessage;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.xl.common.config.autoconfig.properties.SmsProperties.SMS_EXCHANGE;
import static com.xl.common.config.autoconfig.properties.SmsProperties.SMS_ROUTING_KEY;

@Service
@DubboService
public class SmsServiceToolImpl implements SmsServiceTool {

    RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;

    public SmsServiceToolImpl(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void broadcast(String message) {
        sendMqMessage(message,null);
    }

    @Override
    public void sendTo(String message, String msisdn) {
        List<String> msisdns = new ArrayList<>();
        msisdns.add(msisdn);
        sendMqMessage(message,msisdns);
    }


    /**
     * 发送短信到mq，mq监听自动消费
     * @param content
     * @return
     */
    public SmsMessage sendMqMessage(String content, List<String> msisdns) {
        try {
            SmsMessage smsMessage = new SmsMessage(UUID.randomUUID().toString(), content, LocalDateTime.now(), msisdns);
            rabbitTemplate.convertAndSend(
                    SMS_EXCHANGE,
                    SMS_ROUTING_KEY,
                    objectMapper.writeValueAsString(smsMessage)
            );
            System.out.println("Sent SMS to queue: " + smsMessage);
            return smsMessage;
        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
