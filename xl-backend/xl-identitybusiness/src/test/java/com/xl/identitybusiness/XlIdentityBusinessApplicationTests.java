package com.xl.identitybusiness;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.gavlyukovskiy.boot.jdbc.decorator.DecoratedDataSource;
import com.xl.common.dubbo.entity.Customer;
import com.xl.identitybusiness.mapper.CustomerMapper;
import com.xl.identitybusiness.service.impl.CustomerServiceImpl;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.datasource.props.DataSourceProperties;
import org.apache.shardingsphere.infra.metadata.ShardingSphereMetaData;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.mode.metadata.MetaDataContexts;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Map;

@SpringBootTest(classes = {XlIdentityBusinessApplication.class})
class XlIdentityBusinessApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(XlIdentityBusinessApplicationTests.class);

    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    PasswordEncoder passwordEncoder;
    CustomerMapper customerMapper;

    @Autowired
    private DataSource dataSource;  // ShardingSphere 代理数据源
//    @Autowired
//    DataSourceProperties dataSourceProperties;


    @Test
    void saveCustomerTest1() {
        Customer customer = new Customer();
        customer.setId(1231231L);
        customer.setUsername("admin1");
        customer.setPassword("admin");
        int var1 = customerService.saveCustomer(customer);
        System.out.println("********************"+var1);
    }

    @Test
    void saveCustomerTest2() {
        int var1 = customerService.saveCustomer("1231231","admin1","admin");
        System.out.println("********************"+var1);
    }
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


}
