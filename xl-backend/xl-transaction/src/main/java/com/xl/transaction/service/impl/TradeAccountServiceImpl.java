package com.xl.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.common.dubbo.api.TradeAccountService;
import com.xl.common.dubbo.api.CustomerService;
import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.transaction.mapper.TradeAccountMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@DubboService  //被其他service调用
@Service
public class TradeAccountServiceImpl extends ServiceImpl<TradeAccountMapper,TradeAccount> implements TradeAccountService {

    @DubboReference
    private CustomerService customerService;
    @Autowired
    private TradeAccountMapper tradeAccountMapper;

    public TradeAccount getTradeAccountByAccountNo(String accountNo) {
        LambdaQueryWrapper<TradeAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TradeAccount::getAccountNo, accountNo);
        return tradeAccountMapper.selectOne(queryWrapper);
    }

    public TradeAccount getTradeAccountByIdentityId(String identityId) {
        LambdaQueryWrapper<TradeAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TradeAccount::getIdentityId, identityId);
        return tradeAccountMapper.selectOne(queryWrapper);
    }

    @Override
    public TradeAccount createAccount(String identityId) {
        TradeAccount tradeAccount = new TradeAccount();
        tradeAccount.setIdentityId(identityId);
        tradeAccount.setBalance(BigDecimal.valueOf(0));
        tradeAccount.setFrozenAmount(BigDecimal.valueOf(0));
        tradeAccount.setAccountType(1);
        tradeAccountMapper.insert(tradeAccount);
        return getTradeAccountByIdentityId(identityId);
    }

//    @Transactional
//    public ResponseEntity<?> transfer(AccountDto transferRequest) {
//        // 获取转出账户
//        Customer fromCustomer = customerService.getById(transferRequest.getFromAccountId());
//        // 获取转入账户
//        Customer toCustomer = customerService.getById(transferRequest.getToAccountId());
//
//        if (fromCustomer == null || toCustomer == null) {
//            return ResponseEntity.errorResult(ResponseCodeEnum.DATA_NOT_EXIST, "Account not found");
//        }
//
//        // 检查余额是否足够
//        if (fromCustomer.getBalance() < transferRequest.getAmount()) {
//            return ResponseEntity.errorResult(ResponseCodeEnum.PARAM_INVALID, "Insufficient balance");
//        }
//
//        // 执行转账
//        fromCustomer.setBalance(fromCustomer.getBalance() - transferRequest.getAmount());
//        toCustomer.setBalance(toCustomer.getBalance() + transferRequest.getAmount());
//
//        // 更新数据库
//        customerService.updateById(fromCustomer);
//        customerService.updateById(toCustomer);
//
//        return ResponseEntity.okResult("Transfer successful");
//    }
} 