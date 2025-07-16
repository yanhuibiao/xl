-- 创建用户数据库
CREATE DATABASE IF NOT EXISTS xl_users CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建数据库用户
CREATE USER 'user'@'%' IDENTIFIED BY 'password123';
GRANT SELECT, INSERT, UPDATE,delete  ON `xl_users`.* TO 'user'@'%';
FLUSH PRIVILEGES;

-- 使用用户数据库
USE xl_users;
-- 用户表
create table IF NOT EXISTS customer
(
    id varchar(255) PRIMARY KEY not null COMMENT '用户ID',
    username varchar(255) not null COMMENT '用户名',
    age int,
    sex char,
    security_credential varchar(255) not null,
    phone varchar(16),
    status varchar(2) default '02',
    account_id varchar(255),
    identity_type varchar(4),
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    constraint id
        unique (id),
    constraint username
        unique (username)
) COMMENT='用户表';

CREATE TABLE administrator (
    id VARCHAR(255) PRIMARY KEY,  -- 使用MyBatis Plus的IdType.ASSIGN_ID策略
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(2) NOT NULL,  -- 假设IdentityStatus是一个枚举类型，存储为字符串
    phone VARCHAR(255),
    account_id VARCHAR(255),
    role_id VARCHAR(255),
    password_expire_time DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);