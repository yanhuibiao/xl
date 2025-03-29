package com.xl.common.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.common.dubbo.entity.TradeOrder;

import java.math.BigDecimal;


public interface TradeOrderService extends IService<TradeOrder> {
    TradeOrder createTradeOrder(TradeAccount debitAccount, TradeAccount creditAccount, BigDecimal amount, Integer orderStatus);

    TradeOrder getTradeOrderByOrderNo(String orderNo);

    void updateTradeOrder(TradeOrder tradeOrder);
}
