package com.xl.identitybiz.smsservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.xl.common.dubbo.entity.SmsMessage;
import com.xl.identitybiz.smsservice.service.SmsWebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.xl.common.config.autoconfig.properties.SmsProperties.SMS_QUEUE;

@Slf4j
@Component
public class SmsListenerAndConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = SMS_QUEUE)
    public void receiveSms(String message, Channel channel, Message amqpMessage) {
        log.info("Received RabbitMQ message: {}", message);
        try {
            SmsMessage smsMessage = objectMapper.readValue(message, SmsMessage.class);
            System.out.println("Received SMS from queue: " + smsMessage);
            SmsWebSocketService.sendMessage(smsMessage);
            // 手动确认消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.err.println("Failed to process SMS: " + e.getMessage());
            try {
                channel.basicNack(amqpMessage.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception ex) {
                System.err.println("Failed to nack message: " + ex.getMessage());
            }
        }
    }
}