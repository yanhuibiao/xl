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
    id varchar(255) not null COMMENT '用户ID',
    username varchar(255) not null COMMENT '用户名',
    age int,
    sex char,
    security_credential varchar(255) not null,
    phone varchar(16),
    status varchar(2) default '02',
    account_id varchar(255),
    identity_type varchar(4),
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    constraint id
        unique (id),
    constraint username
        unique (username)
) COMMENT='用户表';
alter table customer
    add primary key (id);

