package com.xl.common.dubbo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xl.common.utils.Generator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 账户实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("trade_account")
public class TradeAccount extends BasePojo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */

//    @TableId(value = "id", type = IdType.AUTO)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
     * 乐观锁版本号
     */
    @Version
    private Integer version;

    /**
     * 数据签名
     */
    @JsonIgnore  // 该字段不会出现在 JSON 响应中
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String signature;

    /**
     * 生成签名内容（不包含签名字段本身）
     */
    public String generateSignContent() {
        Objects.requireNonNull(accountNo, "accountNo cannot be null");
        if (Stream.of(identityId, balance, frozenAmount).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Some parameters are null");
        }
        return accountNo + identityId + balance.toString() + frozenAmount.toString();
    }

    public static abstract class TradeAccountStatus {
        public static final Integer PENDING_ACTIVE = 0;
        public static final Integer ACTIVE = 1;
    }
}