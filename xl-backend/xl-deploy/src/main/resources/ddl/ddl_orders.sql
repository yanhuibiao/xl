-- 创建订单数据库
CREATE DATABASE IF NOT EXISTS xl_orders CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用订单数据库
USE xl_orders;

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '商品数量',
    total_price DECIMAL(10, 2) NOT NULL COMMENT '总价格',
    status ENUM('pending', 'completed', 'cancelled') NOT NULL COMMENT '订单状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
    -- FOREIGN KEY (user_id) REFERENCES xl_users.users(id), -- 跨数据库外键需要特殊处理
    -- FOREIGN KEY (product_id) REFERENCES xl_products.products(id) -- 跨数据库外键需要特殊处理
) COMMENT='订单表'; 