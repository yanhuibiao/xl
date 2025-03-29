package com.xl.identitybusiness.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.common.dubbo.api.CustomerService;
import com.xl.common.dubbo.api.TradeAccountService;
import com.xl.common.dubbo.entity.Customer;
import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.common.enums.IdentityStatus;
import com.xl.common.exception.BusinessException;
import com.xl.common.utils.Generator;
import com.xl.identitybusiness.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalLock;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@DubboService(filter = "-seata")
@DubboService
@Service
@Slf4j
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @DubboReference
    TradeAccountService tradeAccountService;


    public int saveCustomer(Customer customer){
        return customerMapper.mybatisSave(customer);
    }

    public int saveCustomer(String id,String username,String password){
        return customerMapper.mybatisSave1(id,username,password);
    }

    /**
     * 注册客户，并创建交易账户
     * name 事务名称
     */
    @Override
    @GlobalTransactional(name = "customer-register-tx", rollbackFor = Exception.class)
    public Map<String, Object> registerCustomer(Customer customer) {
        // 检查用户名是否已存在
        if (!checkUsernameAvailable(customer.getUsername())) {
            throw new BusinessException(401,"客户已经存在");
        }
        // 密码加密
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        // 创建客户实体
        String accountId = Generator.generateNumberId();
        String identityId = Generator.generateNumberId();
        customer.setIdentityType("1000");
        customer.setStatus(IdentityStatus.PendingActive);
        customer.setAccountNo(accountId);
        customer.setIdentityId(identityId);
        // 保存客户信息
        customerMapper.insert(customer);
        log.info("客户注册成功：{}", customer.getId());
        // 调用交易服务创建账户
        TradeAccount account;
        try {
            account = tradeAccountService.createAccount(identityId,accountId);
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(402,"account service error");
        }
        log.info("创建账户成功：{}", account.getAccountNo());
        Map<String, Object> map = new HashMap<>();
        map.put("customer", customer);
        map.put("account", account);
        // 构建返回对象
        return map;
    }

//    @GlobalLock  //告诉Seata不要开启新事务，但仍能让Seata通过XID进行事务控制。
    @Override
    public Customer findCustomerByUsername(String username) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return customerMapper.selectOne(queryWrapper);
    }

    /**
     * 检查用户名是否可用
     */
    public boolean checkUsernameAvailable(String username) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getUsername, username);
        return customerMapper.selectCount(queryWrapper) == 0;
    }
}
