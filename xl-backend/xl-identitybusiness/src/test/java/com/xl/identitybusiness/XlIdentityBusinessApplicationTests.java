package com.xl.identitybusiness;

import com.xl.common.aspect.LogAspect;
import com.xl.common.dubbo.dao.Customer;
import com.xl.identitybusiness.controller.Authentication;
import com.xl.identitybusiness.mapper.CustomerMapper;
import com.xl.identitybusiness.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {XlIdentityBusinessApplication.class})
class XlIdentityBusinessApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(XlIdentityBusinessApplicationTests.class);

    @Autowired
    CustomerServiceImpl customerService;
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

}
