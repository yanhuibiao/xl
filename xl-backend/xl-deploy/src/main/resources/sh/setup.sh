#!/bin/bash

# 更新系统
sudo yum update -y

# 安装 Docker 和 JDK
sudo yum install -y yum-utils
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

# 检查并安装 Docker
if ! command -v docker &> /dev/null
then
  echo "Docker 未安装，开始安装..."
  sudo yum install -y docker-ce docker-ce-cli containerd.io
  sudo systemctl start docker
  sudo systemctl enable docker
else
  echo "Docker 已安装"
fi

# 检查并安装 JDK 17
if ! java -version 2>&1 | grep "17" &> /dev/null
then
  echo "JDK 17 未安装，开始安装..."
  sudo yum install -y java-17-openjdk
else
  echo "JDK 17 已安装"
fi

# 拉取并运行 MySQL
docker pull mysql:8.0
docker run --name mysql -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:8.0
sleep 30 # 等待 MySQL 初始化

# 初始化数据库
docker exec -i mysql mysql -uroot -proot <<EOF
$(cat xl-backend/xl-deploy/src/main/resources/ddl/ddl_users.sql)
$(cat xl-backend/xl-deploy/src/main/resources/ddl/ddl_transactions.sql)
$(cat xl-backend/xl-deploy/src/main/resources/ddl/ddl_products.sql)
$(cat xl-backend/xl-deploy/src/main/resources/ddl/ddl_orders.sql)
$(cat xl-backend/xl-deploy/src/main/resources/ddl/ddl_risk_checks.sql)
$(cat xl-backend/xl-deploy/src/main/resources/ddl/ddl_notifications.sql)
EOF

# 拉取并运行 Redis
docker pull redis:latest
docker run --name redis -d -p 6379:6379 redis:latest

# 拉取并运行 RabbitMQ
docker pull rabbitmq:3-management
docker run --name rabbitmq -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# 拉取并运行 Nginx
docker pull nginx:latest
docker run --name nginx -d -p 80:80 nginx:latest