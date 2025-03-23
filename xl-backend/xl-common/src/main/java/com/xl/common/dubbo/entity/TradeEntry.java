package com.xl.common.dubbo.entity;

import com.alibaba.csp.sentinel.transport.heartbeat.SimpleHttpHeartbeatSender;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易流水实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("trade_entry")
public class TradeEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 交易流水号
     */
    @TableField(fill = FieldFill.INSERT)
    private String entryNo;

    /**
     * 账户编号
     */
    private String accountNo;

    /**
     * 关联账户编号
     */
    private String relatedAccountNo;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易类型：1-充值，2-提现，3-转账，4-消费，5-退款
     */
    private Integer transactionType;

    /**
     * 交易方向：1-收入，2-支出
     */
    private Integer direction;

    /**
     * 交易前余额
     */
    private BigDecimal balanceBefore;

    /**
     * 交易后余额
     */
    private BigDecimal balanceAfter;

    /**
     * 交易状态：0-处理中，1-成功，2-失败
     */
    private Integer status;

    /**
     * 关联订单号
     */
    private String orderNo;

    /**
     * 交易描述
     */
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 数据签名
     */
    private String signature;

    /**
     * 生成签名内容（不包含签名字段本身）
     */
    public String generateSignContent() {
        return id + entryNo + accountNo + (relatedAccountNo != null ? relatedAccountNo : "") +
               amount.toString() + transactionType + direction + balanceBefore.toString() + 
               balanceAfter.toString() + status + (orderNo != null ? orderNo : "") + 
               (description != null ? description : "");
    }
} 