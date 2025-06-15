package com.xl.common.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xl.common.dubbo.entity.TradeAccount;

import java.math.BigDecimal;


public interface TradeAccountService extends IService<TradeAccount> {
    TradeAccount createAccount(String identityId, String accountNo);

    TradeAccount getTradeAccountByAccountNo(String accountNo);

    TradeAccount getTradeAccountByIdentityId(String identityId);

    TradeAccount updateAccountBalance(TradeAccount tradeAccount, BigDecimal amount);

    TradeAccount updateAccountFrozenAmount(TradeAccount tradeAccount, BigDecimal amount);

    TradeAccount updateAccount(TradeAccount tradeAccount);
}
