-- 创建交易数据库
CREATE DATABASE IF NOT EXISTS xl_account_0 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS xl_account_1 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 使用交易数据库
USE xl_account_0;

-- 交易表
CREATE TABLE `trade_account_0` (
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

CREATE TABLE `trade_account_1` (
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
USE xl_account_1;

-- 交易表
CREATE TABLE `trade_account_0` (
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

CREATE TABLE `trade_account_1` (
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