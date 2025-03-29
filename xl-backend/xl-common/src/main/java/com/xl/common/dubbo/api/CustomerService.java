package com.xl.common.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xl.common.dubbo.entity.Customer;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Map;

public interface CustomerService extends IService<Customer> {
    public Map<String, Object> registerCustomer(Customer customer);
    Customer findCustomerByUsername(String username);
}
