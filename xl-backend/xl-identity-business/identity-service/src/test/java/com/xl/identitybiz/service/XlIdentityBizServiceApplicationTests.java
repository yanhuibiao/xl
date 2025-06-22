package com.xl.identitybiz.service;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DecoratedDataSource;
import com.xl.common.config.autoconfig.properties.SmsProperties;
import com.xl.common.utils.RabbitMQUtil;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;

@SpringBootTest(classes = {XlIdentityBizServiceApplication.class})
class XlIdentityBizServiceApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(XlIdentityBizServiceApplicationTests.class);


    @Autowired
    RabbitMQUtil rabbitMQUtil;
    PasswordEncoder passwordEncoder;
    @Autowired
    private DataSource dataSource;  // ShardingSphere 代理数据源
//    @Autowired
//    DataSourceProperties dataSourceProperties;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Binding smsBinding;


    @Test
    void test01(){
        String encode = passwordEncoder.encode("123456");
        boolean encode1 = passwordEncoder.matches("123456","$2a$10$i79oHfgNPQQK8hOz23juC.ZSGHx2LujEgLsMlS9epSW6dDB5T9jnK");
        System.out.println("********************"+encode);
        System.out.println("********************"+encode1);
    }

    @Test
    void shouldMatchConfiguredDataSourceProperties() throws SQLException {
        // 获取实际数据源集合
        ShardingSphereDataSource shardingSphereDataSource = (ShardingSphereDataSource) ((DecoratedDataSource) dataSource).getRealDataSource();
        try {
            // 获取ContextManager
            Field contextManagerField = ShardingSphereDataSource.class.getDeclaredField("contextManager");
            contextManagerField.setAccessible(true);
            ContextManager contextManager = (ContextManager) contextManagerField.get(shardingSphereDataSource);
            // 获取元数据
            ShardingSphereMetaData metaData = contextManager.getMetaDataContexts().getMetaData(); // 使用你的schema名称

//            // 打印配置信息
            System.out.println("数据源: " + metaData);
        } catch (Exception e) {
            throw new RuntimeException("获取ShardingSphere配置失败", e);
        }
//        // 遍历验证每个数据源
//        actualDataSources.forEach((name, ds) -> {
//            // 根据连接池类型转换（支持 Hikari/Druid）
//            if (ds instanceof HikariDataSource) {
//                HikariDataSource hikariDs = (HikariDataSource) ds;
//                System.out.println(hikariDs.getJdbcUrl());
//                System.out.println(hikariDs.getUsername());
//            } else if (ds instanceof DruidDataSource) {
//                DruidDataSource druidDs = (DruidDataSource) ds;
//                System.out.println(druidDs.getDriverClassName());
//                System.out.println(druidDs.getUsername());
//            }
//        });
    }

    @Test
    void testRabbitMQUtil1() throws Exception {
        rabbitMQUtil.sendMessage("queueName_1","hello world！");
    }

    @Test
    void testRabbitMQUtil2() throws Exception {
        rabbitMQUtil.sendMessage("exchange_name","key1","hello world！","queueName_1");
    }

    @Test
    void testRabbitMQUtil3() throws Exception {
        rabbitMQUtil.monitorAndConsume("queueName_1", new RabbitMQUtil.MessageHandler() {
            @Override
            public boolean handleMessage(String message, long deliveryTag) throws Exception {
                System.out.println(message + "*****" +deliveryTag);
                return true;
            }
        });
    }

    @Test
    void testRabbitMQUtil4() throws Exception {
        rabbitMQUtil.consumeMessage("queueName_1", (consumerTag, delivery)->{
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received message: " + message);
            try {
                rabbitMQUtil.channelPool.borrowObject().basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    void testRabbitMQUtil5() throws Exception {
        String jsonMessage = "{\"content\":\"Hello\"}";
        rabbitMQUtil.sendMessage(SmsProperties.SMS_EXCHANGE,SmsProperties.SMS_ROUTING_KEY, jsonMessage,SmsProperties.SMS_QUEUE);
        System.out.println(rabbitTemplate.getExchange());
        System.out.println(rabbitTemplate.getRoutingKey());
//        rabbitTemplate.convertAndSend(SmsProperties.SMS_EXCHANGE,SmsProperties.SMS_ROUTING_KEY, jsonMessage);
    }


}
