#!/bin/bash

# 更新系统
#sudo yum update -y

# 安装 Docker 和 JDK
#sudo yum install -y yum-utils
#sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

# 检查并安装 Docker
#if ! command -v docker &> /dev/null
#then
#  echo "Docker 未安装，开始安装..."
#  sudo yum install -y docker-ce docker-ce-cli containerd.io
#  sudo systemctl start docker
#  sudo systemctl enable docker
#else
#  echo "Docker 已安装"
#fi

# 检查并安装 JDK 17
if ! java -version 2>&1 | grep "17" &> /dev/null
then
  echo "JDK 17 未安装，开始安装..."
  sudo yum install -y java-17-openjdk
else
  echo "JDK 17 已安装"
fi
#安装K8s
# 在master节点和worker节点都要执行
#   腾讯云 docker hub 镜像 export REGISTRY_MIRROR="https://mirror.ccs.tencentyun.com"
#   DaoCloud 镜像 export REGISTRY_MIRROR="http://f1361db2.m.daocloud.io"
#   华为云镜像 export REGISTRY_MIRROR="https://05f073ad3c0010ea0f4bc00b7105ec20.mirror.swr.myhuaweicloud.com"
#   阿里云 docker hub 镜像
export REGISTRY_MIRROR=https://registry.cn-hangzhou.aliyuncs.com
#   最后一个参数 1.19.5 用于指定 kubenetes 版本，支持所有 1.19.x 版本的安装
curl -sSL https://kuboard.cn/install-script/v1.19.x/install_kubelet.sh | sh -s 1.19.5
# 初始化master节点，只在master节点执行，替换x.x.x.x为master节点实际 IP（请使用内网 IP）
#   export命令只在当前shell会话中有效，开启新的shell窗口后，如果要继续安装过程，请重新执行此处的export命令
export MASTER_IP=x.x.x.x
#   替换apiserver.demo为您想要的dnsName
export APISERVER_NAME=apiserver.demo
#   Kubernetes容器组所在的网段，该网段安装完成后，由kubernetes创建，事先并不存在于您的物理网络中
export POD_SUBNET=10.100.0.1/16
echo "${MASTER_IP}    ${APISERVER_NAME}" >> /etc/hosts
curl -sSL https://kuboard.cn/install-script/v1.19.x/init_master.sh | sh -s 1.19.5


# 初始化数据库
docker exec -i mysql mysql -uroot -proot <<EOF
$(cat xl-backend/xl-devops/src/main/resources/ddl/ddl_users.sql)
$(cat xl-backend/xl-devops/src/main/resources/ddl/ddl_transactions.sql)
$(cat xl-backend/xl-devops/src/main/resources/ddl/ddl_products.sql)
$(cat xl-backend/xl-devops/src/main/resources/ddl/ddl_orders.sql)
$(cat xl-backend/xl-devops/src/main/resources/ddl/ddl_risk_checks.sql)
$(cat xl-backend/xl-devops/src/main/resources/ddl/ddl_notifications.sql)
EOF
