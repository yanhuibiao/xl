package com.xl.common.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xl.common.dubbo.entity.TempTransaction;


public interface TempTransactionService extends IService<TempTransaction> {
    TempTransaction getTempTransactionByXid(String xid);
    int createTempTransaction(TempTransaction tempTransaction);
    void updateTempTransaction(TempTransaction tempTransaction);
}
