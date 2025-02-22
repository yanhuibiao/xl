-- 创建交易数据库
CREATE DATABASE IF NOT EXISTS xl_transactions CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用交易数据库
USE xl_transactions;

-- 交易表
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '交易ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(10, 2) NOT NULL COMMENT '交易金额',
    transaction_type ENUM('transfer', 'receive', 'bill') NOT NULL COMMENT '交易类型',
    status ENUM('pending', 'completed', 'failed') NOT NULL COMMENT '交易状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
    -- FOREIGN KEY (user_id) REFERENCES xl_users.users(id) -- 跨数据库外键需要特殊处理
) COMMENT='交易表'; 