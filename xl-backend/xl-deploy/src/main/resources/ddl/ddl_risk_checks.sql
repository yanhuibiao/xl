-- 创建风控数据库
CREATE DATABASE IF NOT EXISTS xl_risk_checks CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用风控数据库
USE xl_risk_checks;

-- 风控记录表
CREATE TABLE IF NOT EXISTS risk_checks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '风控记录ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    transaction_id BIGINT NOT NULL COMMENT '交易ID',
    risk_level ENUM('low', 'medium', 'high') NOT NULL COMMENT '风险等级',
    checked_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '检查时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
    -- FOREIGN KEY (user_id) REFERENCES xl_users.users(id), -- 跨数据库外键需要特殊处理
    -- FOREIGN KEY (transaction_id) REFERENCES xl_transactions.transactions(id) -- 跨数据库外键需要特殊处理
) COMMENT='风控记录表'; 