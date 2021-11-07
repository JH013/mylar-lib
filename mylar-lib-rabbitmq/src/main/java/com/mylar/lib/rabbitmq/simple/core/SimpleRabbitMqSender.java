package com.mylar.lib.rabbitmq.simple.core;

import com.mylar.lib.rabbitmq.simple.core.constant.SimpleRabbitMqConstant;
import com.mylar.lib.rabbitmq.simple.core.utils.SimpleRabbitMqConnectionFactoryUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * RabbitMQ生产者
 *
 * @author wangz
 * @date 2021/11/7 0007 20:46
 */
@Component(SimpleRabbitMqConstant.BEAN_NAME_SENDER)
public class SimpleRabbitMqSender {

    /**
     * 构造方法
     */
    public SimpleRabbitMqSender() {

    }

    // region 变量

    /**
     * RabbitMQ配置属性
     */
    private RabbitProperties rabbitProperties;

    /**
     * MQ连接
     */
    private ConnectionFactory cachingConnectionFactory;

    /**
     * template
     */
    private RabbitTemplate rabbitTemplate;

    // endregion

    // region 公共方法

    /**
     * 初始化生产者
     *
     * @param rabbitProperties RabbitMQ配置属性
     */
    public void init(RabbitProperties rabbitProperties) {

        this.rabbitProperties = rabbitProperties;

        // 创建生产者连接池
        this.cachingConnectionFactory = SimpleRabbitMqConnectionFactoryUtil.getCachingConnectionFactory(rabbitProperties, SimpleRabbitMqConstant.CONNECTION_SCOPE_SENDER);

        // 初始化 RabbitTemplate
        this.initRabbitTemplate(cachingConnectionFactory);

        // 初始化延时队列
        this.initDelayQueue();

    }

    /**
     * 发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     */
    public void send(String exchange, String routingKey, String message) {

        // MQ消息属性，设置头部信息
        MessageProperties messageProperties = new MessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();
        headers.put(SimpleRabbitMqConstant.HEADER_KEY_MEMBER_NAME, "Mylar");

        // MQ消息，附加头部信息
        Message mqMessage = new Message(message.getBytes(StandardCharsets.UTF_8), messageProperties);

        // 关联数据，添加消息进去，用于发送确认失败后处理
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        correlationData.setReturnedMessage(mqMessage);

        // 发送消息
        this.rabbitTemplate.convertAndSend(exchange, routingKey, mqMessage, correlationData);
    }

    // endregion

    // 私有方法

    /**
     * 初始化 RabbitTemplate
     *
     * @param cachingConnectionFactory MQ连接
     */
    private void initRabbitTemplate(ConnectionFactory cachingConnectionFactory) {

        // 新建 Template，用于发送
        this.rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        // 发送确认：消息发送到交换机的结果回调
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.out.println(String.format("message not arrive exchange, cause: %s", cause));
                if (correlationData != null) {
                    System.out.println(String.format("return message: %s", correlationData.getReturnedMessage() == null ? "" :
                            correlationData.getReturnedMessage().toString()));
                }
            }
        });

        // 消息通过交换器无法匹配到队列会返回给生产者
        this.rabbitTemplate.setMandatory(true);

        // 失败回调：消息从交换机到队列的结果回调（Exchange -> Queue），失败后回调，如路由键不存在
        this.rabbitTemplate.setReturnsCallback(returnedMessage -> {
            if (returnedMessage.getReplyCode() != 200) {
                System.out.println(String.format("message not arrive queue, return message: %s", returnedMessage.toString()));
            }
        });
    }

    /**
     * 初始化延时队列
     */
    private void initDelayQueue() {
        this.rabbitTemplate.execute((ChannelCallback<Void>) channel -> {

            // 死信参数
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", SimpleRabbitMqConstant.EXCHANGE_SIMPLE_DELAY);
            arguments.put("x-dead-letter-routing-key", SimpleRabbitMqConstant.ROUTING_KEY_SIMPLE_DELAY);

            // 声明死信交换机
            String dlxExchange = SimpleRabbitMqConstant.DLX_EXCHANGE_SIMPLE;
            channel.exchangeDeclare(dlxExchange, BuiltinExchangeType.DIRECT, true, false, null);

            // 延时时间集合，根据时间绑定多个延时队列
            List<Long> delayTimes = new ArrayList<>();
            delayTimes.add(10000L);
            delayTimes.add(20000L);
            delayTimes.forEach(t -> {
                String queueName = SimpleRabbitMqConstant.DLX_QUEUE_PREFIX + t;
                arguments.put("x-message-ttl", t);
                try {
                    channel.queueDeclare(queueName, true, false, false, arguments);
                    channel.queueBind(queueName, dlxExchange, SimpleRabbitMqConstant.DLX_ROUTING_KEY_PREFIX + t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            return null;
        });
    }

    // endregion

}
