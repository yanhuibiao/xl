package com.xl.transactionservice.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xl.common.dubbo.api.*;
import com.xl.common.dubbo.entity.TempTransaction;
import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.common.dubbo.entity.TradeEntry;
import com.xl.common.dubbo.entity.TradeOrder;
import com.xl.common.exception.BusinessException;
import com.xl.common.utils.Generator;
import com.xl.transactionservice.service.TransferTCCService;
import org.apache.catalina.core.ApplicationContext;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.rm.tcc.api.BusinessActionContextParameter;
import org.apache.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferTCCServiceImpl implements TransferTCCService {


    public TradeAccountService tradeAccountService;
    public TradeOrderService tradeOrderService;
    public TradeEntryService tradeEntryService;
    public TempTransactionService tempTransactionService;

    public TransferTCCServiceImpl(TradeAccountService tradeAccountService, TradeOrderService tradeOrderService, TradeEntryService tradeEntryService, TempTransactionService tempTransactionService) {
        this.tradeAccountService = tradeAccountService;
        this.tradeOrderService = tradeOrderService;
        this.tradeEntryService = tradeEntryService;
        this.tempTransactionService = tempTransactionService;
    }

    // TCC实现类这里必须添加@GlobalTransactional，不然不会调confirm和rollback,
    // 不知道为啥这里seata一直使用CGLIB代理，没能使用JDK动态代理，所以要在实现类上加上TwoPhaseBusinessAction注解
    @Override
    @GlobalTransactional(lockRetryInterval = 5000, lockRetryTimes = 3)
    @TwoPhaseBusinessAction(name = "transferTCCAction",commitMethod = "commit",rollbackMethod = "rollback")
    public void transferPrepare(@BusinessActionContextParameter(paramName = "debitAccount") TradeAccount debitAccount,
                                @BusinessActionContextParameter(paramName = "creditAccount") TradeAccount creditAccount,
                                @BusinessActionContextParameter(paramName = "amount") BigDecimal amount) {
        // 判断是否空回滚,避免业务悬挂,只有try和cancel会创建这个entry，如果存在就说明已经cancel了
        String xid = RootContext.getXID();
        if (tempTransactionService.getTempTransactionByXid(xid) != null) {
            return;
        }
        // 判断余额是否充足
        if (amount.compareTo(debitAccount.getBalance()) > 0) {
            throw new BusinessException(401,"The balance is insufficient");
        }
        // 更新debit的余额
        tradeAccountService.updateAccountBalance(debitAccount, debitAccount.getBalance().subtract(amount));
        // 更新debit的冻结金额
        tradeAccountService.updateAccountFrozenAmount(debitAccount, debitAccount.getFrozenAmount().add(amount));
        // 更新credit的余额
        tradeAccountService.updateAccountBalance(creditAccount, creditAccount.getBalance().add(amount));
        // 创建订单
        TradeOrder tradeOrder = tradeOrderService.createTradeOrder(debitAccount,creditAccount,amount,TradeOrder.OrderStatus.PAYING);
        // 生成流水
        tradeEntryService.createTradeEntry(tradeOrder);
        // 写入临时事务表
        TempTransaction tempTransaction = new TempTransaction();
        tempTransaction.setXid(xid).setStatus(TempTransaction.TempTransactionstatus.TRY).setContext(tradeOrder.getOrderNo());
        tempTransactionService.createTempTransaction(tempTransaction);
    }

    @Override
    @Transactional(timeout = 6000)
    public boolean commit(BusinessActionContext context) {
        String xid = context.getXid();
        TempTransaction tempTransaction = tempTransactionService.getTempTransactionByXid(xid);
        // 修改订单状态
        TradeOrder tradeOrder = tradeOrderService.getTradeOrderByOrderNo(tempTransaction.getContext());
        tradeOrder.setOrderStatus(TradeOrder.OrderStatus.COMPLETE);
        tradeOrderService.updateTradeOrder(tradeOrder);
        // 从事务上下文中获取参数
        TradeAccount debitAccount = JSON.parseObject(((JSONObject) context.getActionContext("debitAccount")).toJSONString(), TradeAccount.class);
        debitAccount = tradeAccountService.getTradeAccountByAccountNo(debitAccount.getAccountNo());
        // 减去冻结金额
        debitAccount.setFrozenAmount(debitAccount.getFrozenAmount().subtract((BigDecimal) context.getActionContext("amount")));
        tradeAccountService.updateAccountFrozenAmount(debitAccount, debitAccount.getFrozenAmount());
        // 修改临时事务表状态为comfirm
        tempTransaction.setStatus(TempTransaction.TempTransactionstatus.CONFIRM);
        tempTransactionService.updateTempTransaction(tempTransaction);
        return true;
    }

    @Override
    @Transactional
    public boolean rollback(BusinessActionContext context) {
        String xid = context.getXid();
        TempTransaction tempTransaction = tempTransactionService.getTempTransactionByXid(xid);
        // 空回滚处理
        if (tempTransaction == null) {
            TempTransaction tempTransaction1 = new TempTransaction();
            tempTransaction1.setXid(xid).setStatus(TempTransaction.TempTransactionstatus.CANCEL);
            tempTransactionService.createTempTransaction(tempTransaction1);
            return true;
        }
        // 幂等处理
        if (tempTransaction.getStatus() == TempTransaction.TempTransactionstatus.CANCEL) {
            return true;
        }
        BigDecimal amount = (BigDecimal) context.getActionContext("amount");
        TradeAccount debitAccount = JSON.parseObject(((JSONObject) context.getActionContext("debitAccount")).toJSONString(), TradeAccount.class);
        TradeAccount creditAccount = JSON.parseObject(((JSONObject) context.getActionContext("creditAccount")).toJSONString(), TradeAccount.class);
        debitAccount = tradeAccountService.getTradeAccountByAccountNo(debitAccount.getAccountNo());
        creditAccount = tradeAccountService.getTradeAccountByAccountNo(creditAccount.getAccountNo());
        // 回滚debit的余额
        tradeAccountService.updateAccountBalance(debitAccount, debitAccount.getBalance().add(amount));
        // 回滚debit的冻结金额
        tradeAccountService.updateAccountFrozenAmount(debitAccount, debitAccount.getFrozenAmount().subtract(amount));
        // 回滚credit的余额
        tradeAccountService.updateAccountBalance(creditAccount, creditAccount.getBalance().subtract(amount));
        // 回滚订单,生成cancel的订单
        TradeOrder tradeOrder = tradeOrderService.getTradeOrderByOrderNo(tempTransaction.getContext());
        tradeOrder.setOrderStatus(TradeOrder.OrderStatus.CANCEL);
        tradeOrderService.updateTradeOrder(tradeOrder);
        // 删除流水
        tradeEntryService.deleteTradeEntry(tempTransaction.getContext());
        // 修改临时事务表的状态为cancel
        tempTransaction.setStatus(TempTransaction.TempTransactionstatus.CANCEL);
        tempTransactionService.updateTempTransaction(tempTransaction);
        return true;
    }
}
