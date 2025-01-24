package com.xl.common.autoconfig;

import com.xl.common.autoconfig.properties.JwtProperties;
import com.xl.common.autoconfig.template.JwtUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties({
        JwtProperties.class
})
public class AutomaticUtilConfig {

    @Bean
    public JwtUtils jwtUtils(JwtProperties jwtProperties){
        return new JwtUtils(jwtProperties);
    }

//    @Bean
//    public SmsTemplate smsTemplate(SmsProperties properties) {
//        return new SmsTemplate(properties);
//    }
//
//    @Bean
//    public OssTemplate ossTemplate(OssProperties properties) {
//        return new OssTemplate(properties);
//    }
//
//    @Bean
//    public AipFaceTemplate aipFaceTemplate() {
//        return new AipFaceTemplate();
//    }
//
//    @Bean
//    public HuanXinTemplate huanXinTemplate(HuanXinProperties properties) {
//        return new HuanXinTemplate(properties);
//    }
//
//    /**
//     * 检测配置文件中，是否具有tanhua.green开头的配置
//     *      同时，其中的enable属性 = true
//     */
//    @Bean
//    @ConditionalOnProperty(prefix = "tanhua.green",value = "enable", havingValue = "true")
//    public AliyunGreenTemplate aliyunGreenTemplate(GreenProperties properties) {
//        return new AliyunGreenTemplate(properties);
//    }

}
