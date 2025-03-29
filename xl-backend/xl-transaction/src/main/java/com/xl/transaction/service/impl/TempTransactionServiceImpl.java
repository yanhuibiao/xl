package com.xl.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xl.common.dubbo.entity.TempTransaction;
import com.xl.transaction.mapper.TempTransactionMapper;
import com.xl.common.dubbo.api.TempTransactionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
public class TempTransactionServiceImpl extends ServiceImpl<TempTransactionMapper, TempTransaction> implements TempTransactionService {
    TempTransactionMapper tempTransactionMapper;

    public TempTransactionServiceImpl(TempTransactionMapper tempTransactionMapper) {
        this.tempTransactionMapper = tempTransactionMapper;
    }

    @Override
    public TempTransaction getTempTransactionByXid(String xid) {
        return tempTransactionMapper.selectByXid(xid);
    }

    @Override
    public int createTempTransaction(TempTransaction tempTransaction) {
        return tempTransactionMapper.insert(tempTransaction);
    }

    @Override
    public void updateTempTransaction(TempTransaction tempTransaction) {
        LambdaUpdateWrapper<TempTransaction> updateWrapper = new LambdaUpdateWrapper<TempTransaction>();
        updateWrapper.eq(TempTransaction::getXid,tempTransaction.getXid());
        tempTransactionMapper.update(tempTransaction, updateWrapper);
    }
}
