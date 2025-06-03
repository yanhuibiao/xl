package com.xl.customercenter;

import com.xl.common.dubbo.entity.Customer;
import com.xl.customercenter.mapper.CustomerMapper;
import com.xl.customercenter.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class XlCustomerCenterApplicationTests {
    @Autowired
    CustomerServiceImpl customerService;

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
}
