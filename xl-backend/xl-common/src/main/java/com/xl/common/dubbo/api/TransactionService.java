package com.xl.common.dubbo.api;

import com.xl.common.dubbo.entity.TradeAccount;

import java.math.BigDecimal;

public interface TransactionService {
    void transfer(TradeAccount debitAccount, TradeAccount creditAccount, BigDecimal amount);
}
