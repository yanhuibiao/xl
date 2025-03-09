package com.xl.transaction.service.impl;

import com.xl.common.entity.AccountDto;
import com.xl.common.entity.ResponseEntity;
import com.xl.common.dubbo.dao.Customer;
import com.xl.common.dubbo.api.CustomerService;
import com.xl.common.enums.ResponseCodeEnum;
import com.xl.transaction.service.TransferService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferServiceImpl implements TransferService {

    @DubboReference
    private CustomerService customerService;

    @Override
    @Transactional
    public ResponseEntity<?> transfer(AccountDto transferRequest) {
        // 获取转出账户
        Customer fromCustomer = customerService.getById(transferRequest.getFromAccountId());
        // 获取转入账户
        Customer toCustomer = customerService.getById(transferRequest.getToAccountId());

        if (fromCustomer == null || toCustomer == null) {
            return ResponseEntity.errorResult(ResponseCodeEnum.DATA_NOT_EXIST, "Account not found");
        }

        // 检查余额是否足够
        if (fromCustomer.getBalance() < transferRequest.getAmount()) {
            return ResponseEntity.errorResult(ResponseCodeEnum.PARAM_INVALID, "Insufficient balance");
        }

        // 执行转账
        fromCustomer.setBalance(fromCustomer.getBalance() - transferRequest.getAmount());
        toCustomer.setBalance(toCustomer.getBalance() + transferRequest.getAmount());

        // 更新数据库
        customerService.updateById(fromCustomer);
        customerService.updateById(toCustomer);

        return ResponseEntity.okResult("Transfer successful");
    }
} 