package com.xl.common.config.autoconfig.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

// ignoreInvalidFields:直接在配置类中，设置属性的默认值，表示当这个配置不存在或者设置非法时，使用默认的配置
@Data
@ConfigurationProperties(value = "spring.jwt",ignoreInvalidFields = true)
public class JwtProperties {
    String secret = "xlbb";
    int expiration = 30;
}
