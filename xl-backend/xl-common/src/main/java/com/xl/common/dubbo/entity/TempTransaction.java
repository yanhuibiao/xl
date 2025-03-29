package com.xl.common.dubbo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分布式事务表实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("temp_transaction")
public class TempTransaction extends BasePojo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 全局事务ID
     */
    @TableField("xid")
    private String xid;

    /**
     * 事务上下文
     */
    @TableField("context")
    private String context;

    /**
     * 事务状态 0:try  1:confirm 2:cancel
     */
    @TableField("status")
    private String status;

    /**
     * 事务负载数据（JSON格式）
     */
    @TableField("payload")
    private String payload;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Long retryCount;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    public static abstract class TempTransactionstatus {
        public static final String TRY = "0";
        public static final String CONFIRM = "1";
        public static final String CANCEL = "2";

    }
} 