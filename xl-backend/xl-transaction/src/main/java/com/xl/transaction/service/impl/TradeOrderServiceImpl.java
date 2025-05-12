package com.xl.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.common.dubbo.api.TradeAccountService;
import com.xl.common.dubbo.api.TradeOrderService;
import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.common.dubbo.entity.TradeOrder;
import com.xl.common.utils.Generator;
import com.xl.transaction.mapper.TradeEntryMapper;
import com.xl.transaction.mapper.TradeOrderMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@DubboService
@Service
public class TradeOrderServiceImpl extends ServiceImpl<TradeOrderMapper, TradeOrder> implements TradeOrderService {

    TradeOrderMapper tradeOrderMapper;

    public TradeOrderServiceImpl(TradeOrderMapper tradeOrderMapper) {
        this.tradeOrderMapper = tradeOrderMapper;
    }

    @Override
    public TradeOrder createTradeOrder(TradeAccount debitAccount, TradeAccount creditAccount, BigDecimal amount, Integer orderStatus) {
        TradeOrder tradeOrder = new TradeOrder();
        String orderNo = Generator.generateNumberId();
        tradeOrder.setPayerId(debitAccount.getIdentityId()).setPayeeId(creditAccount.getIdentityId())
                .setDebitAccountNo(debitAccount.getAccountNo()).setCreditAccountNo(creditAccount.getAccountNo()).setOrderAmount(amount)
                .setPayAmount(amount).setOrderType(1).setOrderStatus(orderStatus).setOrderNo(orderNo);
        tradeOrderMapper.insert(tradeOrder);
        return getTradeOrderByOrderNo(orderNo);
    }

    @Override
    public TradeOrder getTradeOrderByOrderNo(String orderNo) {
        QueryWrapper<TradeOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return tradeOrderMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateTradeOrder(TradeOrder tradeOrder) {
        UpdateWrapper<TradeOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_no", tradeOrder.getOrderNo())
                .eq("payer_id", tradeOrder.getPayerId());  //这里加payer_id条件是因为分表规则依赖这个字段
        tradeOrderMapper.update(tradeOrder,updateWrapper);
    }

}
