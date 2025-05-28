package com.xl.common;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.xl.common.config.autoconfig.properties.RabbitMQProperties;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class RabbitMQConnectionFactory extends BasePooledObjectFactory<Connection> {
    private final ConnectionFactory connectionFactory;

    public RabbitMQConnectionFactory(RabbitMQProperties properties) {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(properties.getHost());
        this.connectionFactory.setPort(properties.getPort());
        this.connectionFactory.setUsername(properties.getUsername());
        this.connectionFactory.setPassword(properties.getPassword());
        this.connectionFactory.setVirtualHost(properties.getVirtualHost());
        this.connectionFactory.setAutomaticRecoveryEnabled(properties.isAutomaticRecovery());
        this.connectionFactory.setConnectionTimeout(properties.getConnectionTimeout());
        this.connectionFactory.setNetworkRecoveryInterval(properties.getNetworkRecoveryInterval());
    }

    /**
     * 创建对象：通过create()方法在对象池中创建新的对象
     * @return
     * @throws Exception
     */
    @Override
    public Connection create() throws Exception {
        return connectionFactory.newConnection();
    }

    /**
     * 包装对象：通过wrap()方法将自定义对象包装成 PooledObject，以便进行状态管理和统计。
     * @param connection
     * @return
     */
    @Override
    public PooledObject<Connection> wrap(Connection connection) {
        return new DefaultPooledObject<>(connection);
    }

    /**
     *销毁对象：通过destroyObject()方法销毁不再需要的对象。
     * @param p
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<Connection> p) throws Exception {
        Connection connection = p.getObject();
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }

    /**
     *校验对象：通过validateObject()方法校验对象是否可用
     * @param p
     * @return
     */
    @Override
    public boolean validateObject(PooledObject<Connection> p) {
        Connection connection = p.getObject();
        return connection != null && connection.isOpen();
    }


}