package com.xl.common.dubbo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("trade_account")
public class TradeAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账户编号
     */
    @TableField(fill = FieldFill.INSERT)
    private String accountNo;

    /**
     * 用户ID
     */
    private String identityId;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 账户状态：0-冻结，1-正常
     */
    private Integer accountStatus;

    /**
     * 账户类型：1-个人，2-企业
     */
    private Integer accountType;

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
     * 乐观锁版本号
     */
    @Version
    private Integer version;

    /**
     * 数据签名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String signature;

    /**
     * 生成签名内容（不包含签名字段本身）
     */
    public String generateSignContent() {
        return id + accountNo + identityId + balance.toString() + frozenAmount.toString() +
                accountStatus + accountType + version;
    }
}