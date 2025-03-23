package com.xl.common.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xl.common.dubbo.entity.TradeAccount;



public interface TradeAccountService extends IService<TradeAccount> {
    public TradeAccount createAccount(String userid);
}
