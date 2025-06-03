package com.xl.transactionservice.controller;

import com.xl.common.dto.ResponseEntity;
import com.xl.common.dubbo.api.CustomerService;
import com.xl.common.dubbo.api.TradeAccountService;
import com.xl.common.dubbo.api.TransactionService;
import com.xl.common.dubbo.entity.Administrator;
import com.xl.common.dubbo.entity.Customer;
import com.xl.common.dubbo.entity.TradeAccount;
import com.xl.common.exception.BusinessException;
import com.xl.transactionservice.service.TransferTCCService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.spring.annotation.GlobalLock;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/transfer")
@Tag(name = "交易管理", description = "交易相关操作API")
public class TransactionController {

    @DubboReference
    CustomerService customerService;
    @DubboReference
    TradeAccountService tradeAccountService;
    TransferTCCService transferTCCService;

    public TransactionController(TransferTCCService transferTCCService) {
        this.transferTCCService = transferTCCService;
    }

    @Operation(summary = "交易接口", description = "返回交易双规账户信息")
//    @ApiResponse(responseCode = "200", description = "交易成功")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "交易成功"),
            @ApiResponse(responseCode = "401", description = "交易失败")
    })
    @PostMapping("")
    public ResponseEntity<?> transfer(@RequestBody @Parameter(description = "交易双方的username", required = true) Map map ) {
        // 判断传过来的参数是否符合要求
        String debitUsername;
        String creditUsername;
        BigDecimal amount;
        try {
            debitUsername = map.get("debitUsername").toString();
            creditUsername = map.get("creditUsername").toString();
            amount = BigDecimal.valueOf(Double.parseDouble(map.get("amount").toString()));
        }catch (Exception e){
            return ResponseEntity.errorResult(401,"Required parameters are missing");
        }
        // 判断交易双方是否一致
        if (Objects.equals(debitUsername, creditUsername)) {
            return ResponseEntity.errorResult(401,"The parties to the transaction cannot be the same");
        }
        // 判断交易双方的客户是否存在系统中
        Customer debitCustomer = customerService.findCustomerByUsername(debitUsername);
        Customer creditCustomer = customerService.findCustomerByUsername(creditUsername);
        if (Objects.isNull(debitCustomer) || Objects.isNull(creditCustomer)) {
            return ResponseEntity.errorResult(401,"Customer not exist");
        }
        // 判断交易账户是否存在
        TradeAccount  debitAccount= tradeAccountService.getTradeAccountByAccountNo(debitCustomer.getAccountNo());
        TradeAccount  creditAccount= tradeAccountService.getTradeAccountByAccountNo(creditCustomer.getAccountNo());
        if (Objects.isNull(debitAccount) || Objects.isNull(creditAccount)) {
            return ResponseEntity.errorResult(401,"Transfer account not found");
        }
        // 发起交易
        transferTCCService.transferPrepare(debitAccount,creditAccount, amount);

        HashMap<String,Object> map1 = new HashMap<>();
        map1.put("debitCustomer",debitCustomer);
        map1.put("creditCustomer",creditCustomer);
        map1.put("amount",amount);
        map1.put("msg","Transfer success");
        return ResponseEntity.okResult(map1);
    }

    @PostMapping("/topup")
    public ResponseEntity<?> topup(@RequestBody Map requestBody) {
        String customerUsername;
        BigDecimal amount;
        try {
            customerUsername = requestBody.get("customerUsername").toString();
            amount = BigDecimal.valueOf(Double.parseDouble(requestBody.get("amount").toString()));
        }catch (Exception e){
            return ResponseEntity.errorResult(401,"Required parameters are missing");
        }
        Customer customer = customerService.findCustomerByUsername(customerUsername);
        if (Objects.isNull(customer)) {
            return ResponseEntity.errorResult(401,"Customer not exist");
        }
        String accountNo = customer.getAccountNo();
        TradeAccount tradeAccount = tradeAccountService.getTradeAccountByAccountNo(accountNo);
        if (tradeAccount == null) {
            return ResponseEntity.errorResult(401,"Account not exist");
        }
        if (!Objects.equals(tradeAccount.getAccountStatus(), TradeAccount.TradeAccountStatus.ACTIVE)) {
            return ResponseEntity.errorResult(401,"Account not active");
        }
        return ResponseEntity.okResult(
                tradeAccountService.updateAccountBalance(tradeAccount, tradeAccount.getBalance().add(amount)));
    }

}
