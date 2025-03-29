package com.xl.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.common.dubbo.api.TradeAccountService;
import com.xl.common.dubbo.api.TradeEntryService;
import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.common.dubbo.entity.TradeEntry;
import com.xl.common.dubbo.entity.TradeOrder;
import com.xl.transaction.mapper.TradeEntryMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@DubboService
@Service
public class TradeEntryServiceImpl extends ServiceImpl<TradeEntryMapper, TradeEntry> implements TradeEntryService {

    TradeEntryMapper tradeEntryMapper;
    TradeAccountService tradeAccountService;
    public TradeEntryServiceImpl(TradeEntryMapper tradeEntryMapper,TradeAccountService tradeAccountService) {
        this.tradeEntryMapper = tradeEntryMapper;
        this.tradeAccountService = tradeAccountService;
    }

    @Override
    public void createTradeEntry(TradeOrder tradeOrder) {
        TradeEntry debitTradeEntry = new TradeEntry();
        TradeEntry creditTradeEntry = new TradeEntry();
        TradeAccount debitTradeAccount = tradeAccountService.getTradeAccountByAccountNo(tradeOrder.getDebitAccountNo());
        TradeAccount creditTradeAccount = tradeAccountService.getTradeAccountByAccountNo(tradeOrder.getCreditAccountNo());
        debitTradeEntry.setAccountNo(tradeOrder.getDebitAccountNo()).setAmount(tradeOrder.getOrderAmount())
                .setBalanceBefore(debitTradeAccount.getBalance()).setBalanceAfter(debitTradeAccount.getBalance().subtract(tradeOrder.getOrderAmount()))
                .setOrderNo(tradeOrder.getOrderNo()).setRelatedAccountNo(tradeOrder.getCreditAccountNo()).setDirection("D");
        creditTradeEntry.setAccountNo(tradeOrder.getCreditAccountNo()).setAmount(tradeOrder.getOrderAmount())
                .setBalanceBefore(creditTradeAccount.getBalance())
                .setBalanceAfter(creditTradeAccount.getBalance().add(tradeOrder.getOrderAmount()))
                .setOrderNo(tradeOrder.getOrderNo()).setRelatedAccountNo(tradeOrder.getDebitAccountNo()).setDirection("C");
        tradeEntryMapper.insert(debitTradeEntry);
        tradeEntryMapper.insert(creditTradeEntry);
    }

    @Override
    public List<TradeEntry> getTradeEntry(String orderNo) {
        QueryWrapper<TradeEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return tradeEntryMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteTradeEntry(String orderNo) {
        QueryWrapper<TradeEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        tradeEntryMapper.delete(queryWrapper);
    }
}
