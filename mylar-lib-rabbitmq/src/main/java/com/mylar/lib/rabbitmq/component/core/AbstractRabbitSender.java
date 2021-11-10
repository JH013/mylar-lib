package com.mylar.lib.rabbitmq.component.core;

import com.mylar.lib.rabbitmq.component.constant.RabbitConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * RabbitMQ生产者抽象基类
 *
 * @author wangz
 * @date 2021/11/10 0010 0:31
 */
public abstract class AbstractRabbitSender {

    /**
     * 生产者委托
     */
    private RabbitSenderDelegate delegate;

    /**
     * 发送消息
     *
     * @param memberName 会员名
     * @param message    消息体
     */
    public void send(String memberName, String message) {

        // 交换机 & 路由键
        String exchange = delegate.getExchange();
        String routingKey = delegate.getRoutingKey();

        // MQ消息属性，设置头部信息
        MessageProperties messageProperties = new MessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();
        headers.put(RabbitConstant.HEADER_KEY_MEMBER_NAME, memberName);

        // MQ消息，附加头部信息
        Message mqMessage = new Message(message.getBytes(StandardCharsets.UTF_8), messageProperties);

        // 关联数据，添加消息进去，用于发送确认失败后处理
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        correlationData.setReturnedMessage(mqMessage);

        this.delegate.getRabbitTemplate().send(exchange, routingKey, mqMessage, correlationData);
    }

    /**
     * 设置生产者委托
     *
     * @param delegate 生产者委托
     */
    public void setDelegate(RabbitSenderDelegate delegate) {
        this.delegate = delegate;
    }
}
