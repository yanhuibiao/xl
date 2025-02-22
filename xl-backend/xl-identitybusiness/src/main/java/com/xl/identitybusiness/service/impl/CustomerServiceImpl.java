package com.xl.identitybusiness.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.common.dubbo.api.CustomerService;
import com.xl.common.dubbo.dao.Customer;
import com.xl.identitybusiness.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@DubboService
@Service
@Slf4j
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    public int saveCustomer(Customer customer){
        return customerMapper.mybatisSave(customer);
    }

    public int saveCustomer(String id,String username,String password){
        return customerMapper.mybatisSave1(id,username,password);
    }

}
