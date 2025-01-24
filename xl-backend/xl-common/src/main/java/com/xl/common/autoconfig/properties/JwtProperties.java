package com.xl.common.autoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

// ignoreInvalidFields:直接在配置类中，设置属性的默认值，表示当这个配置不存在或者设置非法时，使用默认的配置
@Data
@ConfigurationProperties(value = "timeout.jwt-token",ignoreInvalidFields = true)
public class JwtProperties {
    int sessionTimeout = 30;
    String secret = "xl";
}
