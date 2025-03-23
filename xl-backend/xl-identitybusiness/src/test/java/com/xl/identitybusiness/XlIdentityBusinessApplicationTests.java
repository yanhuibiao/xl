package com.xl.identitybusiness;

import com.xl.common.dubbo.entity.Customer;
import com.xl.identitybusiness.mapper.CustomerMapper;
import com.xl.identitybusiness.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = {XlIdentityBusinessApplication.class})
class XlIdentityBusinessApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(XlIdentityBusinessApplicationTests.class);

    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    PasswordEncoder passwordEncoder;
    CustomerMapper customerMapper;

    @Test
    void saveCustomerTest1() {
        Customer customer = new Customer();
        customer.setId("1231231");
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

}
