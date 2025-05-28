package com.xl.common.config.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq1", ignoreInvalidFields = true)
public class RabbitMQProperties {
    private String host = "192.168.224.128";
    private int port = 5672;
    private String username = "admin";
    private String password = "admin";
    private String virtualHost = "/";
    private int connectionTimeout = 30000;
    private int requestedChannelMax = 2047;
    private int requestedFrameMax = 0;
    private int requestedHeartbeat = 60;
    private boolean automaticRecovery = true;
    private int networkRecoveryInterval = 5000;

}