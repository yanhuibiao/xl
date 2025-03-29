package com.xl.transaction.service;

import com.xl.common.dubbo.entity.TradeAccount;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.rm.tcc.api.BusinessActionContextParameter;
import org.apache.seata.rm.tcc.api.LocalTCC;
import org.apache.seata.rm.tcc.api.TwoPhaseBusinessAction;
import java.math.BigDecimal;

@LocalTCC  // 只有在本地调用才用这个注解
public interface TransferTCCService {

    // TCC必须返回boolean类型的数据,TCC模式要求Try阶段的方法第一个参数必须是 BusinessActionContext  --错误
    @TwoPhaseBusinessAction(name = "transferTCCAction",commitMethod = "commit",rollbackMethod = "rollback")
    void transferPrepare(@BusinessActionContextParameter(paramName = "debitAccount") TradeAccount debitAccount,
                         @BusinessActionContextParameter(paramName = "creditAccount") TradeAccount creditAccount,
                         @BusinessActionContextParameter(paramName = "amount") BigDecimal amount);

    boolean commit(BusinessActionContext context);

    boolean rollback(BusinessActionContext context);
}
