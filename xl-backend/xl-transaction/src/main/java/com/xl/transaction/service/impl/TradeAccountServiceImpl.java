package com.xl.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

    @Override
    public TradeAccount createAccount(String identityId, String accountNo) {
        TradeAccount tradeAccount = new TradeAccount();
        tradeAccount.setIdentityId(identityId);
        tradeAccount.setAccountNo(accountNo);
        tradeAccount.setBalance(BigDecimal.valueOf(0));
        tradeAccount.setFrozenAmount(BigDecimal.valueOf(0));
        tradeAccount.setAccountType(1);
        tradeAccountMapper.insert(tradeAccount);
        return getTradeAccountByIdentityId(identityId);
    }

    @Override
    public TradeAccount getTradeAccountByAccountNo(String accountNo) {
        LambdaQueryWrapper<TradeAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TradeAccount::getAccountNo, accountNo);
        return tradeAccountMapper.selectOne(queryWrapper);
    }

    @Override
    public TradeAccount getTradeAccountByIdentityId(String identityId) {
        LambdaQueryWrapper<TradeAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TradeAccount::getIdentityId, identityId);
        return tradeAccountMapper.selectOne(queryWrapper);
    }

    @Override
    public TradeAccount updateAccountBalance(TradeAccount tradeAccount, BigDecimal amount) {
        UpdateWrapper<TradeAccount> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("account_no", tradeAccount.getAccountNo());
        tradeAccount.setSignature(null);
        tradeAccountMapper.update(tradeAccount.setBalance(amount), updateWrapper);
        // 传入对象才会触发mybatis plus的自动更新字段的操作
//        tradeAccountMapper.update(updateWrapper);
        return getTradeAccountByAccountNo(tradeAccount.getAccountNo());
    }

    @Override
    public TradeAccount updateAccountFrozenAmount(TradeAccount tradeAccount, BigDecimal amount) {
        UpdateWrapper<TradeAccount> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("account_no", tradeAccount.getAccountNo()).set("frozen_amount", amount);
        // 如果entry类set字段的属性有值，还是以updateWrapper的set优先
        tradeAccountMapper.update(tradeAccount.setSignature(null), updateWrapper);
        return getTradeAccountByAccountNo(tradeAccount.getAccountNo());
    }

} 