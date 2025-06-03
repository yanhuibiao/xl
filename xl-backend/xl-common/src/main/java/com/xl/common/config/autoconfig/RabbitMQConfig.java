package com.xl.common.config.autoconfig;

import com.xl.common.config.autoconfig.properties.RabbitMQProperties;
import com.xl.common.config.autoconfig.properties.SmsProperties;
import com.xl.common.utils.RabbitMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQUtil.class);


    // 交换器定义
    @Bean
    public DirectExchange businessExchange() {
        return new DirectExchange("business.exchange", true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("dead.letter.exchange", true, false);
    }

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed.exchange", "x-delayed-message", true, false, args);
    }

    // 队列定义
    @Bean
    public Queue businessQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换器
        args.put("x-dead-letter-exchange", "dead.letter.exchange");
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", "dead.letter.routing.key");
        // 设置队列最大长度
        args.put("x-max-length", 10000);
        // 设置消息TTL(毫秒)
        args.put("x-message-ttl", 60000);
        return QueueBuilder.durable("business.queue").withArguments(args).build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("dead.letter.queue").build();
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable("delayed.queue").build();
    }

    // 绑定定义
    @Bean
    public Binding businessBinding() {
        return BindingBuilder.bind(businessQueue())
                .to(businessExchange())
                .with("business.routing.key");
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("dead.letter.routing.key");
    }

    @Bean
    public Binding delayedBinding() {
        return BindingBuilder.bind(delayedQueue())
                .to(delayedExchange())
                .with("delayed.routing.key").noargs();
    }

    @Bean
    public Binding smsBinding() {
        DirectExchange smsExchange = new DirectExchange(SmsProperties.SMS_EXCHANGE, true, false);
        Queue smsQueue = QueueBuilder.durable(SmsProperties.SMS_QUEUE).build();
        return BindingBuilder.bind(smsQueue)
                .to(smsExchange)
                .with(SmsProperties.SMS_ROUTING_KEY);
    }

    // 消息转换器
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate 配置
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        // 消息发送确认回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("Message sent successfully: {}", correlationData);
            } else {
                log.error("Message send failed: {}, cause: {}", correlationData, cause);
                throw new RuntimeException("Message not acknowledged");
            }
        });
        // 消息不可达回调
        template.setReturnsCallback((returnedMessage) -> {
            log.error("消息不可达: {}, 返回码: {}, 返回文本: {}, 交换器: {}, 路由键: {}", returnedMessage.getMessage(),
                    returnedMessage.getReplyCode(), returnedMessage.getReplyText(), returnedMessage.getExchange(),
                    returnedMessage.getRoutingKey());
        });
        return template;
    }

    // 监听器容器工厂配置
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认
        factory.setConcurrentConsumers(5); // 最小消费者数量
        factory.setMaxConcurrentConsumers(10); // 最大消费者数量
        factory.setPrefetchCount(50); // 每个消费者最大预取消息数
        factory.setDefaultRequeueRejected(false); // 拒绝消息时不重新入队
        // 异常处理
        factory.setErrorHandler(t -> {
            log.error("RabbitMQ监听发生异常", t);
        });
        return factory;
    }

    @Bean(destroyMethod = "close")
    public RabbitMQUtil rabbitMQUtil(RabbitMQProperties rabbitMQProperties) {
        return new RabbitMQUtil(rabbitMQProperties);
    }

}