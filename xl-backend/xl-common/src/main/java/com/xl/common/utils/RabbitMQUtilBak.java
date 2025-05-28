package com.xl.common.utils;

import com.rabbitmq.client.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitMQUtilBak implements InitializingBean, DisposableBean {

    private static final String RABBITMQ_HOST = "192.168.224.128";
    private static final int RABBITMQ_PORT = 5672;
    private static final String RABBITMQ_USERNAME = "admin";
    private static final String RABBITMQ_PASSWORD = "admin";
    private static final String RABBITMQ_VIRTUAL_HOST = "/";
    private static final String DEFAULT_CONNECTION= "default_connection";
    private static final String DEFAULT_CHANNEL = "default_channel";

    // 连接工厂
    public ConnectionFactory factory;
    // 连接和通道缓存（线程安全）
    public ConcurrentHashMap<String, Connection> connection_pool = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Channel> channel_pool = new ConcurrentHashMap<>();
    public boolean USE_PUBLISH_CONFIRM = true;

    /**
     * 当一个 Bean 被 Spring 实例化并且其所有属性都被注入之后，Spring 会自动调用其 afterPropertiesSet() 方法。这时候你可以在该方法中编写一些初始化逻辑
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_HOST);
        factory.setPort(RABBITMQ_PORT);
        factory.setUsername(RABBITMQ_USERNAME);
        factory.setPassword(RABBITMQ_PASSWORD);
        factory.setVirtualHost(RABBITMQ_VIRTUAL_HOST);
        factory.setRequestedHeartbeat(60);
        factory.setConnectionTimeout(30000);
//        this.connection = factory.newConnection();
//        this.channel = connection.createChannel();
    }

    /*** 获取 RabbitMQ 连接
     * @param connectionName 自定义连接名称（用于区分不同连接）
     * @return Connection
     */
    public Connection getConnection(String connectionName) throws IOException, TimeoutException {
        if (!connection_pool.containsKey(connectionName)) {
            Connection connection = factory.newConnection();
            connection_pool.put(connectionName, connection);
        }
        return connection_pool.get(connectionName);
    }

    /**
     * 创建 Channel（自动恢复机制）
     * @param connectionName 连接名称
     * @param channelName    自定义 Channel 名称
     * @return Channel
     */
    public Channel createChannel(String connectionName, String channelName) throws IOException, TimeoutException {
        Connection connection = getConnection(connectionName);
        Channel channel = connection.createChannel();
        channel.confirmSelect(); // 启用手动确认模式
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) {
                System.out.println("Message confirmed, deliveryTag: " + deliveryTag);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) {
                System.err.println("Message nacked, deliveryTag: " + deliveryTag);
            }
        });
        channel_pool.put(channelName, channel);
        return channel;
    }

    /**
     * 如果没有队列则自动创建默认connection，并创建对应名称的channel
     * @param channelName
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public Channel getChannel(String channelName) throws IOException, TimeoutException {
        Channel channel;
        if (!channel_pool.containsKey(channelName)) {
            channel = createChannel(DEFAULT_CONNECTION, channelName);
        }else {
            channel = channel_pool.get(channelName);
        }
        return channel;
    }

    /**
     * 声明交换机
     * 1、交换机名称
     * 2、交换机类型：direct、topic、fanout、headers
     * 3、指定交换机是否需要持久化，true为需要持久化交换机的元数据
     * 4、指定交换机在没有队列绑定时，是否需要自动删除，默认为false
     * 5、Map<String,Object>类型，用来指定交换机其他的一些机构化参数，默认null
     */
    public void declareExchange(String exchange, BuiltinExchangeType type) throws IOException, TimeoutException {
        Channel channel = getChannel(DEFAULT_CHANNEL);
        channel.exchangeDeclare(exchange, type, true,false,null);
    }

    /**
     * 声明队列
     * 1、队列名称
     * 2、队列是否需要持久化，这里持久化的是队列名称等元数据的持久化，不是队列中的消息持久
     * 3、表示对列是否私有的，如果是私有的只有创建它的应用程序才能消费消息
     * 4、队列在没有消费者订阅的情况下是否删除，默认为false
     * 5、队列的一些结构化信息，比如死信队列、磁盘队列
     */
    public void declareQueue(String queueName) throws IOException, TimeoutException {
        Channel channel = getChannel(DEFAULT_CHANNEL);
        channel.queueDeclare(queueName, true, false, false, null);
    }

    /**
     * 绑定队列和交换机
     */
    public void bindQueue(String queueName, String exchange, String routingKey) throws IOException, TimeoutException {
        Channel channel = getChannel(DEFAULT_CHANNEL);
        channel.queueBind(queueName, exchange, routingKey);
    }


    /**
     * 发送消息
     */
    public void sendMessage(String exchange, String routingKey, String message) throws IOException, TimeoutException {
        Channel channel = getChannel(DEFAULT_CHANNEL);
        channel.basicPublish(exchange, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 消费消息（自动ACK）
     */
    public void consume(String queueName, DeliverCallback deliverCallback) throws IOException, TimeoutException {
        Channel channel = getChannel(DEFAULT_CHANNEL);
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    /**
     * 实现 DisposableBean 接口的类，会在 Spring 容器销毁该 Bean 时 自动调用其 destroy() 方法
     */
    @Override
    public void destroy() throws Exception {
        for (Channel channel : channel_pool.values()) {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        };
        for (Connection connection : connection_pool.values()) {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
        };
    }


    /**
     * 关闭指定的连接和channel资源
     * @param connectionName 连接名称
     * @param channelName    Channel 名称
     */
    public void closeResources(String connectionName, String channelName) {
        try {
            if (channelName != null && channel_pool.containsKey(channelName)) {
                channel_pool.get(channelName).close();
                channel_pool.remove(channelName);
            }
            if (connectionName != null && connection_pool.containsKey(connectionName)) {
                connection_pool.get(connectionName).close();
                connection_pool.remove(connectionName);
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息（支持持久化）
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     * @param message      消息内容
     * @param properties   消息属性（可设置 TTL、优先级等）
     */
    public void sendMessage(String exchangeName, String routingKey,
                                   Object message, AMQP.BasicProperties properties) throws IOException, TimeoutException {
        Channel channel = null;
        try {
            channel = createChannel("default", "send-channel");
            // 消息序列化（建议使用 JSON）
            byte[] body = message.toString().getBytes();
            // 发送消息
            channel.basicPublish(exchangeName, routingKey, properties != null ? properties : MessageProperties.PERSISTENT_TEXT_PLAIN, body);
            System.out.println(" [x] Sent '" + message + "' to " + exchangeName);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            // 处理发送失败（可加入重试机制）
        } finally {
            // 发送完成后不需要关闭 Channel（保持复用）
            if (channel != null) {
                channel.close();
            }
        }
    }

    /**
     * 消费消息（手动确认）
     *
     * @param queueName      队列名称
     * @param messageHandler 消息处理器
     */
    public void consumeMessage(String queueName, MessageHandler messageHandler) {
        Channel channel = null;
        try {
            channel = createChannel("default", "consume-channel");
            channel.basicQos(1); // 公平分发，每次只处理一条消息

            Channel finalChannel = channel;
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    // 处理消息
                    messageHandler.handle(delivery.getBody());
                    // 手动确认消息
                    finalChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
                    // 处理失败进行拒绝（可配置重试或进入死信队列）
                    finalChannel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            };

            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }


    /**
     * 消息处理器接口
     */
    public interface MessageHandler {
        void handle(byte[] messageBody);
    }

}