package com.xl.transaction.service.impl;

import com.xl.common.dto.ResponseEntity;
import com.xl.common.dubbo.api.TransactionService;
import com.xl.common.dubbo.entity.Customer;
import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.common.exception.BusinessException;
import com.xl.transaction.service.TransferTCCService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

@DubboService
@Service
public class TransactionServiceImpl implements TransactionService {

    public TransferTCCService transferTCCService;

    public TransactionServiceImpl(TransferTCCService transferTCCService) {
        this.transferTCCService = transferTCCService;
    }

    @Override
    public void transfer(TradeAccount debitAccount, TradeAccount creditAccount, BigDecimal amount) {
        if (Objects.equals(debitAccount.getAccountNo(), creditAccount.getAccountNo())) {
            throw new BusinessException(401,"The parties to the transaction cannot be the same");
        }
        transferTCCService.transferPrepare(debitAccount, creditAccount, amount);
    }
}
