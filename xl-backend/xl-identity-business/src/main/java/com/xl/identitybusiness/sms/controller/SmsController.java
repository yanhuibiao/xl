package com.xl.identitybusiness.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xl.common.dto.ResponseEntity;
import com.xl.common.dubbo.entity.SmsMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.xl.common.config.autoconfig.properties.SmsProperties.SMS_EXCHANGE;
import static com.xl.common.config.autoconfig.properties.SmsProperties.SMS_ROUTING_KEY;


@RestController
@RequestMapping("/ws")
public class SmsController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/broadcast")
    public ResponseEntity broadcast(@RequestParam String message) {
        sendMqMessage(message,null);
        return ResponseEntity.okResult("SMS sent successfully");
    }

    @PostMapping("/sendTo/{msisdn}")
    public ResponseEntity sendTo(@PathVariable String msisdn, @RequestParam String message) {
        List<String> msisdns = new ArrayList<>();
        msisdns.add(msisdn);
        sendMqMessage(message,msisdns);
        return ResponseEntity.okResult("SMS sent successfully");
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