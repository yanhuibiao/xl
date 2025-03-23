-- 创建交易数据库
CREATE DATABASE IF NOT EXISTS xl_transaction CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用交易数据库
USE xl_transaction;

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

CREATE TABLE `trade_entry` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `entry_no` varchar(32) NOT NULL COMMENT '交易流水号',
                                 `account_no` varchar(32) NOT NULL COMMENT '账户编号',
                                 `related_account_no` varchar(32) DEFAULT NULL COMMENT '关联账户编号',
                                 `amount` decimal(20,2) NOT NULL COMMENT '交易金额',
                                 `transaction_type` tinyint(4) NOT NULL COMMENT '交易类型：1-充值，2-提现，3-转账，4-消费，5-退款',
                                 `direction` tinyint(4) NOT NULL COMMENT '交易方向：1-收入，2-支出',
                                 `balance_before` decimal(20,2) NOT NULL COMMENT '交易前余额',
                                 `balance_after` decimal(20,2) NOT NULL COMMENT '交易后余额',
                                 `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '交易状态：0-处理中，1-成功，2-失败',
                                 `order_no` varchar(32) DEFAULT NULL COMMENT '关联订单号',
                                 `description` varchar(255) DEFAULT NULL COMMENT '交易描述',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `signature` varchar(64) NOT NULL COMMENT '数据签名',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `idx_transaction_no` (`entry_no`),
                                 KEY `idx_account_no` (`account_no`),
                                 KEY `idx_order_no` (`order_no`),
                                 KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易流水表';

CREATE TABLE `trade_order` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `order_no` varchar(32) NOT NULL COMMENT '订单编号',
                           `identity_id` varchar(32) NOT NULL COMMENT '用户ID',
                           `account_no` varchar(32) NOT NULL COMMENT '支付账户编号',
                           `order_amount` decimal(20,2) NOT NULL COMMENT '订单金额',
                           `pay_amount` decimal(20,2) NOT NULL COMMENT '实际支付金额',
                           `order_type` tinyint(4) NOT NULL COMMENT '订单类型：1-商品订单，2-服务订单，3-充值订单',
                           `order_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态：0-待支付，1-已支付，2-已取消，3-已退款',
                           `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
                           `transaction_no` varchar(32) DEFAULT NULL COMMENT '交易流水号',
                           `expire_time` datetime DEFAULT NULL COMMENT '订单过期时间',
                           `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `signature` varchar(64) NOT NULL COMMENT '数据签名',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `idx_order_no` (`order_no`),
                           KEY `idx_user_id` (`identity_id`),
                           KEY `idx_account_no` (`account_no`),
                           KEY `idx_create_time` (`create_time`),
                           KEY `idx_order_status` (`order_status`),
                           KEY `idx_transaction_no` (`transaction_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';