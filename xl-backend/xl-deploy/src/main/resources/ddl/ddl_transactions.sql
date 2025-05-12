-- 创建交易数据库
CREATE DATABASE IF NOT EXISTS xl_transaction_0 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS xl_transaction_1 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 使用交易数据库
USE xl_transaction_0;

-- 交易表
CREATE TABLE `trade_account` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `account_no` varchar(32) NOT NULL COMMENT '账户编号',
                             `identity_id` varchar(32) NOT NULL COMMENT '用户ID',
                             `balance` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
                             `frozen_amount` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
                             `account_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '账户状态：0-冻结，1-正常',
                             `account_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '账户类型：1-个人，2-企业',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
                             `signature` varchar(64) NOT NULL COMMENT '数据签名',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `idx_account_no` (`account_no`),
                             KEY `idx_user_id` (`identity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表';

CREATE TABLE `trade_entry_0` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `entry_no` varchar(32) NOT NULL COMMENT '交易流水号',
                                 `account_no` varchar(32) NOT NULL COMMENT '账户编号',
                                 `related_account_no` varchar(32) NOT NULL COMMENT '关联账户编号',
                                 `amount` decimal(20,2) NOT NULL COMMENT '交易金额',
                                 `transaction_type` tinyint(4) DEFAULT NULL COMMENT '交易类型：1-充值，2-提现，3-转账，4-消费，5-退款',
                                 `direction` varchar(1) NOT NULL COMMENT '交易方向：C-收入，D-支出',
                                 `balance_before` decimal(20,2) NOT NULL COMMENT '交易前余额',
                                 `balance_after` decimal(20,2) NOT NULL COMMENT '交易后余额',
                                 `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '交易状态：0-处理中，1-成功，2-失败',
                                 `order_no` varchar(32) NOT NULL COMMENT '关联订单号',
                                 `description` varchar(255) DEFAULT NULL COMMENT '交易描述',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_transaction_no` (`entry_no`),
                                 KEY `idx_account_no` (`account_no`),
                                 KEY `idx_order_no` (`order_no`),
                                 KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易流水表';
CREATE TABLE `trade_entry_1` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `entry_no` varchar(32) NOT NULL COMMENT '交易流水号',
                                 `account_no` varchar(32) NOT NULL COMMENT '账户编号',
                                 `related_account_no` varchar(32) NOT NULL COMMENT '关联账户编号',
                                 `amount` decimal(20,2) NOT NULL COMMENT '交易金额',
                                 `transaction_type` tinyint(4) DEFAULT NULL COMMENT '交易类型：1-充值，2-提现，3-转账，4-消费，5-退款',
                                 `direction` varchar(1) NOT NULL COMMENT '交易方向：C-收入，D-支出',
                                 `balance_before` decimal(20,2) NOT NULL COMMENT '交易前余额',
                                 `balance_after` decimal(20,2) NOT NULL COMMENT '交易后余额',
                                 `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '交易状态：0-处理中，1-成功，2-失败',
                                 `order_no` varchar(32) NOT NULL COMMENT '关联订单号',
                                 `description` varchar(255) DEFAULT NULL COMMENT '交易描述',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_transaction_no` (`entry_no`),
                                 KEY `idx_account_no` (`account_no`),
                                 KEY `idx_order_no` (`order_no`),
                                 KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易流水表';
CREATE TABLE `trade_order_0` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `order_no` varchar(32) NOT NULL COMMENT '订单编号',
                           `payer_id` varchar(32) NOT NULL COMMENT 'debit用户ID',
                           `payee_id` varchar(32) NOT NULL COMMENT 'credit用户ID',
                           `debit_account_no` varchar(32) NOT NULL COMMENT '支付账户编号',
                           `credit_account_no` varchar(32) NOT NULL COMMENT '收款账户编号',
                           `order_amount` decimal(20,2) NOT NULL COMMENT '订单金额',
                           `pay_amount` decimal(20,2) NOT NULL COMMENT '实际支付金额',
                           `order_type` tinyint(4) DEFAULT NULL COMMENT '订单类型：1-商品订单，2-服务订单，3-充值订单',
                           `order_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态：0-待支付，1-待审批，2-完成，3-取消，4-已退款',
                           `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
                           `transaction_no` varchar(32) DEFAULT NULL COMMENT '交易流水号',
                           `expire_time` datetime DEFAULT NULL COMMENT '订单过期时间',
                           `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `idx_order_no` (`order_no`),
                           KEY `idx_payer_id` (`payer_id`),
                           KEY `idx_payee_id` (`payee_id`),
                           KEY `idx_debit_account_no` (`debit_account_no`),
                           KEY `idx_credit_account_no` (`credit_account_no`),
                           KEY `idx_create_time` (`create_time`),
                           KEY `idx_order_status` (`order_status`),
                           KEY `idx_transaction_no` (`transaction_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
CREATE TABLE `trade_order_1` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `order_no` varchar(32) NOT NULL COMMENT '订单编号',
                               `payer_id` varchar(32) NOT NULL COMMENT 'debit用户ID',
                               `payee_id` varchar(32) NOT NULL COMMENT 'credit用户ID',
                               `debit_account_no` varchar(32) NOT NULL COMMENT '支付账户编号',
                               `credit_account_no` varchar(32) NOT NULL COMMENT '收款账户编号',
                               `order_amount` decimal(20,2) NOT NULL COMMENT '订单金额',
                               `pay_amount` decimal(20,2) NOT NULL COMMENT '实际支付金额',
                               `order_type` tinyint(4) DEFAULT NULL COMMENT '订单类型：1-商品订单，2-服务订单，3-充值订单',
                               `order_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态：0-待支付，1-待审批，2-完成，3-取消，4-已退款',
                               `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
                               `transaction_no` varchar(32) DEFAULT NULL COMMENT '交易流水号',
                               `expire_time` datetime DEFAULT NULL COMMENT '订单过期时间',
                               `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `idx_order_no` (`order_no`),
                               KEY `idx_payer_id` (`payer_id`),
                               KEY `idx_payee_id` (`payee_id`),
                               KEY `idx_debit_account_no` (`debit_account_no`),
                               KEY `idx_credit_account_no` (`credit_account_no`),
                               KEY `idx_create_time` (`create_time`),
                               KEY `idx_order_status` (`order_status`),
                               KEY `idx_transaction_no` (`transaction_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE `temp_transaction` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `xid` varchar(100) NOT NULL,
                            `context` varchar(128),
                            `status` bigint NOT NULL,
                            `payload` varchar(128) COMMENT '事务负载数据（JSON格式）',
                            `retry_count` bigint DEFAULT 0 COMMENT '重试次数',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_temp_transaction` (`xid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='分布式事务表';


-- 使用交易数据库
USE xl_transaction_1;

-- 交易表
CREATE TABLE `trade_account` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `account_no` varchar(32) NOT NULL COMMENT '账户编号',
                                 `identity_id` varchar(32) NOT NULL COMMENT '用户ID',
                                 `balance` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
                                 `frozen_amount` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
                                 `account_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '账户状态：0-冻结，1-正常',
                                 `account_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '账户类型：1-个人，2-企业',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
                                 `signature` varchar(64) NOT NULL COMMENT '数据签名',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_account_no` (`account_no`),
                                 KEY `idx_user_id` (`identity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表';

CREATE TABLE `trade_entry_0` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `entry_no` varchar(32) NOT NULL COMMENT '交易流水号',
                                 `account_no` varchar(32) NOT NULL COMMENT '账户编号',
                                 `related_account_no` varchar(32) NOT NULL COMMENT '关联账户编号',
                                 `amount` decimal(20,2) NOT NULL COMMENT '交易金额',
                                 `transaction_type` tinyint(4) DEFAULT NULL COMMENT '交易类型：1-充值，2-提现，3-转账，4-消费，5-退款',
                                 `direction` varchar(1) NOT NULL COMMENT '交易方向：C-收入，D-支出',
                                 `balance_before` decimal(20,2) NOT NULL COMMENT '交易前余额',
                                 `balance_after` decimal(20,2) NOT NULL COMMENT '交易后余额',
                                 `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '交易状态：0-处理中，1-成功，2-失败',
                                 `order_no` varchar(32) NOT NULL COMMENT '关联订单号',
                                 `description` varchar(255) DEFAULT NULL COMMENT '交易描述',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_transaction_no` (`entry_no`),
                                 KEY `idx_account_no` (`account_no`),
                                 KEY `idx_order_no` (`order_no`),
                                 KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易流水表';
CREATE TABLE `trade_entry_1` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `entry_no` varchar(32) NOT NULL COMMENT '交易流水号',
                                 `account_no` varchar(32) NOT NULL COMMENT '账户编号',
                                 `related_account_no` varchar(32) NOT NULL COMMENT '关联账户编号',
                                 `amount` decimal(20,2) NOT NULL COMMENT '交易金额',
                                 `transaction_type` tinyint(4) DEFAULT NULL COMMENT '交易类型：1-充值，2-提现，3-转账，4-消费，5-退款',
                                 `direction` varchar(1) NOT NULL COMMENT '交易方向：C-收入，D-支出',
                                 `balance_before` decimal(20,2) NOT NULL COMMENT '交易前余额',
                                 `balance_after` decimal(20,2) NOT NULL COMMENT '交易后余额',
                                 `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '交易状态：0-处理中，1-成功，2-失败',
                                 `order_no` varchar(32) NOT NULL COMMENT '关联订单号',
                                 `description` varchar(255) DEFAULT NULL COMMENT '交易描述',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_transaction_no` (`entry_no`),
                                 KEY `idx_account_no` (`account_no`),
                                 KEY `idx_order_no` (`order_no`),
                                 KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易流水表';
CREATE TABLE `trade_order_0` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `order_no` varchar(32) NOT NULL COMMENT '订单编号',
                                 `payer_id` varchar(32) NOT NULL COMMENT 'debit用户ID',
                                 `payee_id` varchar(32) NOT NULL COMMENT 'credit用户ID',
                                 `debit_account_no` varchar(32) NOT NULL COMMENT '支付账户编号',
                                 `credit_account_no` varchar(32) NOT NULL COMMENT '收款账户编号',
                                 `order_amount` decimal(20,2) NOT NULL COMMENT '订单金额',
                                 `pay_amount` decimal(20,2) NOT NULL COMMENT '实际支付金额',
                                 `order_type` tinyint(4) DEFAULT NULL COMMENT '订单类型：1-商品订单，2-服务订单，3-充值订单',
                                 `order_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态：0-待支付，1-待审批，2-完成，3-取消，4-已退款',
                                 `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
                                 `transaction_no` varchar(32) DEFAULT NULL COMMENT '交易流水号',
                                 `expire_time` datetime DEFAULT NULL COMMENT '订单过期时间',
                                 `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_order_no` (`order_no`),
                                 KEY `idx_payer_id` (`payer_id`),
                                 KEY `idx_payee_id` (`payee_id`),
                                 KEY `idx_debit_account_no` (`debit_account_no`),
                                 KEY `idx_credit_account_no` (`credit_account_no`),
                                 KEY `idx_create_time` (`create_time`),
                                 KEY `idx_order_status` (`order_status`),
                                 KEY `idx_transaction_no` (`transaction_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
CREATE TABLE `trade_order_1` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `order_no` varchar(32) NOT NULL COMMENT '订单编号',
                                 `payer_id` varchar(32) NOT NULL COMMENT 'debit用户ID',
                                 `payee_id` varchar(32) NOT NULL COMMENT 'credit用户ID',
                                 `debit_account_no` varchar(32) NOT NULL COMMENT '支付账户编号',
                                 `credit_account_no` varchar(32) NOT NULL COMMENT '收款账户编号',
                                 `order_amount` decimal(20,2) NOT NULL COMMENT '订单金额',
                                 `pay_amount` decimal(20,2) NOT NULL COMMENT '实际支付金额',
                                 `order_type` tinyint(4) DEFAULT NULL COMMENT '订单类型：1-商品订单，2-服务订单，3-充值订单',
                                 `order_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态：0-待支付，1-待审批，2-完成，3-取消，4-已退款',
                                 `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
                                 `transaction_no` varchar(32) DEFAULT NULL COMMENT '交易流水号',
                                 `expire_time` datetime DEFAULT NULL COMMENT '订单过期时间',
                                 `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_order_no` (`order_no`),
                                 KEY `idx_payer_id` (`payer_id`),
                                 KEY `idx_payee_id` (`payee_id`),
                                 KEY `idx_debit_account_no` (`debit_account_no`),
                                 KEY `idx_credit_account_no` (`credit_account_no`),
                                 KEY `idx_create_time` (`create_time`),
                                 KEY `idx_order_status` (`order_status`),
                                 KEY `idx_transaction_no` (`transaction_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE `temp_transaction` (
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `xid` varchar(100) NOT NULL,
                                    `context` varchar(128),
                                    `status` bigint NOT NULL,
                                    `payload` varchar(128) COMMENT '事务负载数据（JSON格式）',
                                    `retry_count` bigint DEFAULT 0 COMMENT '重试次数',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `ux_temp_transaction` (`xid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='分布式事务表';