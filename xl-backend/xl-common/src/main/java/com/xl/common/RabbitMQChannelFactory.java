package com.xl.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

public class RabbitMQChannelFactory extends BasePooledObjectFactory<Channel> {
    private final Connection connection;

    public RabbitMQChannelFactory(Connection connection) {
        this.connection = connection;
    }

//    创建对象：通过create()方法在对象池中创建新的对象。
    @Override
    public Channel create() throws Exception {
        Channel channel = connection.createChannel();
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Confirm acknowledged: " + deliveryTag);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Confirm nack acknowledged: " + deliveryTag);
            }
        });
        return channel;
    }

//    包装对象：通过wrap()方法将自定义对象包装成 PooledObject，以便进行状态管理和统计。
    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

//    销毁对象：通过destroyObject()方法销毁不再需要的对象。
    @Override
    public void destroyObject(PooledObject<Channel> p) throws Exception {
        Channel channel = p.getObject();
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }

//    校验对象：通过validateObject()方法校验对象是否可用。
    @Override
    public boolean validateObject(PooledObject<Channel> p) {
        Channel channel = p.getObject();
        return channel != null && channel.isOpen();
    }

//    激活对象：通过 activateObject() 方法激活钝化的对象。
    @Override
    public void passivateObject(PooledObject<Channel> p) throws Exception {
        super.passivateObject(p);
    }

//    钝化对象：通过 passivateObject() 方法钝化未使用的对象
    @Override
    public void activateObject(PooledObject<Channel> p) throws Exception {
        super.activateObject(p);
    }
}