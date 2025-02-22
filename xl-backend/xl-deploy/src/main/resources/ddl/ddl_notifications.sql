-- 创建通知数据库
CREATE DATABASE IF NOT EXISTS xl_notifications CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用通知数据库
USE xl_notifications;

-- 通知表
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    message TEXT NOT NULL COMMENT '通知消息',
    read_status BOOLEAN DEFAULT FALSE COMMENT '阅读状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
    -- FOREIGN KEY (user_id) REFERENCES xl_users.users(id) -- 跨数据库外键需要特殊处理
) COMMENT='通知表'; 