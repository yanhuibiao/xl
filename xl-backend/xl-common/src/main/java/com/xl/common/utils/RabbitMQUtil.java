package com.xl.common.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.xl.common.RabbitMQChannelFactory;
import com.xl.common.RabbitMQConnectionFactory;
import com.xl.common.config.autoconfig.properties.RabbitMQProperties;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RabbitMQUtil {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQUtil.class);
    private final ObjectPool<Connection> connectionPool;
    public final ObjectPool<Channel> channelPool;
    // 定义 consumerTags 用于存储队列与 consumerTag 的映射
    private final Map<String, String> consumerTags = new ConcurrentHashMap<>();
//    private final RabbitMQPoolManager poolManager;

    public RabbitMQUtil(RabbitMQProperties properties) {
        // 连接池配置
        GenericObjectPoolConfig<Connection> connectionPoolConfig = new GenericObjectPoolConfig<>();
        connectionPoolConfig.setMaxTotal(10); // 最大连接数
        connectionPoolConfig.setMaxIdle(5);   // 最大空闲连接数
        connectionPoolConfig.setMinIdle(2);   // 最小空闲连接数
        connectionPoolConfig.setTestOnBorrow(true);
        connectionPoolConfig.setTestOnReturn(true);
        connectionPoolConfig.setTestWhileIdle(true);
        // 通道池配置
        GenericObjectPoolConfig<Channel> channelPoolConfig = new GenericObjectPoolConfig<>();
        channelPoolConfig.setMaxTotal(50);    // 每个连接可以有多个通道
        channelPoolConfig.setMaxIdle(20);
        channelPoolConfig.setMinIdle(5);
        channelPoolConfig.setTestOnBorrow(true);
        channelPoolConfig.setTestOnReturn(true);
        channelPoolConfig.setTestWhileIdle(true);
        // 创建连接池
        this.connectionPool = new GenericObjectPool<>(new RabbitMQConnectionFactory(properties), connectionPoolConfig);
        // 创建通道池
        try {
            Connection connection = connectionPool.borrowObject();
            this.channelPool = new GenericObjectPool<>(new RabbitMQChannelFactory(connection), channelPoolConfig);
        /*
          returnObject 方法用于将使用完毕的对象归还给连接池，以便其他请求可以复用。
          具体行为:
            验证对象：检查对象是否仍然有效（如果配置了验证）
            钝化对象：调用对象的钝化方法（如果配置了）
            销毁无效对象：如果对象已失效，则销毁而非归还
            归还池中：将有效对象放回池中供其他请求使用
            唤醒等待线程：如果有线程在等待对象，则通知它们
         */
            connectionPool.returnObject(connection);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize RabbitMQ channel pool", e);
        }
    }

    /**
     * 发送消息到队列
     */
    public void sendMessage(String queueName, String message) throws Exception {
        Channel channel = null;
        try {
            /*
              borrowObject方法用于从连接池中获取一个可用的对象
              具体行为:
                检查空闲对象：首先查看池中是否有空闲可用的对象
                创建新对象：如果没有可用对象且未达到最大限制，则创建新对象
                等待对象：如果池已满且配置了最大等待时间，则等待其他线程归还对象
                验证对象：在返回对象前验证其有效性（如果配置了验证）
                激活对象：调用对象的激活方法（如果配置了）
             */
            channel = channelPool.borrowObject();
            /*
              声明队列
              1、队列名称
              2、队列是否需要持久化，这里持久化的是队列名称等元数据的持久化，不是队列中的消息持久
              3、表示对列是否私有的，如果是私有的只有创建它的应用程序才能消费消息
              4、队列在没有消费者订阅的情况下是否删除，默认为false
              5、队列的一些结构化信息，比如死信队列、磁盘队列
             */
            channel.queueDeclare(queueName, true, false, false, null);
            /*
              RabbitMQ中的channel.basicPublish方法用于发布消息到指定交换机的基本方法
              第三四个参数缺省默认是false
              1、exchange: 要将消息发布到的交换机名称。
              2、routingKey: 路由键，用于决定消息如何路由到队列。在主题交换机中，#可以匹配零个或多个单词，*可以匹配一个单词。
              3、mandatory: 当设置为true时，如果交换机根据类型和路由键无法找到符合条件的队列，会调用basic.return方法将消息返回给生产者。如果设置为false，在上述情况下，消息会被丢弃。
              4、immediate: 当设置为true时，如果交换机在将消息路由到队列时发现没有消费者，消息不会被放入队列中。
              如果所有与路由键关联的队列都没有消费者，消息会通过basic.return方法返回给生产者。需要注意的是，RabbitMQ服务器不支持此标志。
              5、props: 消息的其他属性，如路由头等。特别需要注意的是BasicProperties.deliveryMode，其中0表示不持久化，1表示持久化。这里的持久化指的是消息的持久化，配合设置了durable=true的通道和队列，可以实现服务器宕机后消息仍然保留。
              6、body: 消息体，是一个字节数组。
             */
            channel.basicPublish("", queueName, false,false,null, message.getBytes());
        } finally {
            if (channel != null) {
                channelPool.returnObject(channel);
            }
        }
    }

    // 发送消息（支持确认机制）
    public void sendMessage(String exchange, String routingKey, String message, String queueName) throws Exception {
        Channel channel = null;
        try {
            channel = channelPool.borrowObject();
            /*
              声明交换机
              1、交换机名称
              2、交换机类型：direct、topic、fanout、headers
              3、指定交换机是否需要持久化，true为需要持久化交换机的元数据
              4、指定交换机在没有队列绑定时，是否需要自动删除，默认为false
              5、Map<String,Object>类型，用来指定交换机其他的一些机构化参数，默认null
             */
            channel.exchangeDeclare(exchange, "direct", true);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchange, routingKey);
            // 开启发布者确认
            channel.confirmSelect();
            // 发送消息
            channel.basicPublish(exchange, routingKey, null, message.getBytes("UTF-8"));
            // 等待确认
            if (channel.waitForConfirms(5000)) {
                logger.info("Message sent successfully: {}", message);
            } else {
                logger.error("Message send failed: {}", message);
                throw new IOException("Message not acknowledged");
            }
        } catch (Exception e) {
            logger.error("Failed to send message: {}", message, e);
            throw e;
        } finally {
            channelPool.returnObject(channel);
        }
    }

    /**
     * 直接消费channel消息
     */
    public void consumeMessage(String queueName, DeliverCallback deliverCallback) throws Exception {
        Channel channel = null;
        try {
            channel = channelPool.borrowObject();
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicQos(1);  //设置每次只消费一条消息
            /*
              basicConsume方法的基本形式是接受队列名称、自动确认消息的布尔值、以及回调接口作为参数
              consumerTag = channel.basicConsume(QUEUE_NAME, True, deliverCallback, cancelCallback)
               在上面例子中，QUEUE_NAME是要监听的队列名称，True表示消息在被接收后会自动发送确认信号给服务器，
               deliverCallback是接收消息后的回调接口，而cancelCallback是当消费者取消订阅时的回调接口。
               basicConsume方法中的回调接口deliverCallback和cancelCallback允许开发者定义在特定事件发生时执行的操作。
               deliverCallback在消息送达时被调用，而cancelCallback在消费者取消订阅时被调用。
               return 服务端生成的消费者标识
             */
            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});
            // 保持通道不关闭，因为消费者需要持续监听
            // 在实际应用中，应该设计更完善的通道管理机制
        } catch (Exception e) {
            logger.error("Failed to start consumer", e);
            throw e;
        }finally {
            // 注意：消费者通常保持长连接，不立即关闭channel和connection
        }
    }

    // 监控并消费消息
    public void monitorAndConsume(String queueName, MessageHandler handler) throws Exception {
        final Channel channel = channelPool.borrowObject();
        try {
            channel.basicQos(1); // 每次处理一条消息
            final String finalQueueName = queueName; // 确保 queueName 是 final
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    logger.info("Consumer [{}] received message: {} from queue: {}", consumerTag, message, finalQueueName);
                    boolean success = handler.handleMessage(message, delivery.getEnvelope().getDeliveryTag());
                    if (success) {
                        //第二个参数一个布尔值，指示是否批量确认消息
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        logger.info("Consumer [{}] acknowledged message, deliveryTag: {}", consumerTag, delivery.getEnvelope().getDeliveryTag());
                    } else {
                        //参数2： 是否应用于多消息。参数3： 是否重新放回队列，否则丢弃或者进入死信队列
                        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                        logger.warn("Consumer [{}] failed to process message, deliveryTag: {}, requeue: true", consumerTag, delivery.getEnvelope().getDeliveryTag());
                    }
                } catch (Exception e) {
                    logger.error("Consumer [{}] error processing message, deliveryTag: {}", consumerTag, delivery.getEnvelope().getDeliveryTag(), e);
                    try {
                        channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                    } catch (IOException ex) {
                        logger.error("Consumer [{}] failed to nack message", consumerTag, ex);
                    }
                }
            };
            // 注册消费者并保存 consumerTag
            String tag = channel.basicConsume(finalQueueName, false, deliverCallback, consumerTag -> {
                logger.info("Consumer [{}] cancelled for queue: {}", consumerTag, finalQueueName);
                consumerTags.remove(finalQueueName);
            });
            consumerTags.put(finalQueueName, tag);
            logger.info("Started monitoring queue: {} with consumerTag: {}", finalQueueName, tag);
        } catch (Exception e) {
            logger.error("Failed to start consumer for queue: {}", queueName, e);
            channelPool.returnObject(channel);
            throw e;
        }
        // 注意：不立即关闭 channel 和 connection，保持长连接
    }

    // 取消消费者
    public void cancelConsumer(String queueName) throws Exception {
        String consumerTag = consumerTags.get(queueName);
        if (consumerTag != null) {
            Channel channel = channelPool.borrowObject();
            try {
                channel.basicCancel(consumerTag);
                logger.info("Cancelled consumer [{}] for queue: {}", consumerTag, queueName);
                consumerTags.remove(queueName);
            } finally {
                channelPool.returnObject(channel);
            }
        } else {
            logger.warn("No consumer found for queue: {}", queueName);
        }
    }


    // 手动确认消息
    public void acknowledgeMessage(Channel channel, long deliveryTag, boolean multiple) throws IOException {
        channel.basicAck(deliveryTag, multiple);
        logger.info("Message acknowledged, deliveryTag: {}", deliveryTag);
    }

    // 拒绝消息
    public void rejectMessage(Channel channel, long deliveryTag, boolean requeue) throws IOException {
        channel.basicNack(deliveryTag, false, requeue);
        logger.warn("Message rejected, deliveryTag: {}, requeue: {}", deliveryTag, requeue);
    }

    /**
     * @Bean(destroyMethod = "close")
     */
    public void close() {
        channelPool.close();
        connectionPool.close();
    }

    // 消息处理接口
    @FunctionalInterface
    public interface MessageHandler {
        boolean handleMessage(String message, long deliveryTag) throws Exception;
    }

}