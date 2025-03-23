package com.xl.common.dubbo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)  //注解用于自动生成 equals() 和 hashCode() 方法,callSuper = false 表示：在生成的两个方法中不考虑父类的属性
@Accessors(chain = true)  //添加此注解后，setter 方法会返回当前对象本身
@TableName("trade_order")
public class TradeOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    @TableField(fill = FieldFill.INSERT)
    private String orderNo;

    /**
     * 用户ID
     */
    private String identityId;

    /**
     * 支付账户编号
     */
    private String accountNo;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     * 订单类型：1-商品订单，2-服务订单，3-充值订单
     */
    private Integer orderType;

    /**
     * 订单状态：0-待支付，1-已支付，2-已取消，3-已退款
     */
    private Integer orderStatus;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 交易流水号
     */
    private String transactionNo;

    /**
     * 订单过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 订单备注
     */
    private String remark;

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
        return id + orderNo + identityId + accountNo + orderAmount.toString() + payAmount.toString() +
               orderType + orderStatus + (payTime != null ? payTime.toString() : "") + 
               (transactionNo != null ? transactionNo : "") + 
               (expireTime != null ? expireTime.toString() : "") + 
               (remark != null ? remark : "");
    }
} 