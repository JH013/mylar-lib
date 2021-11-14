package com.mylar.lib.rabbitmq.component.core;

import com.mylar.lib.rabbitmq.component.data.RabbitMessage;
import com.mylar.lib.rabbitmq.component.data.RabbitSenderDelegate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.nio.charset.StandardCharsets;
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
     * @param exchange      交换机
     * @param routingKey    路由键
     * @param rabbitMessage 消息
     */
    public void send(String exchange, String routingKey, RabbitMessage rabbitMessage) {

        // 消息体
        String messageBody = rabbitMessage.getMessageBody();

        // 消息属性
        MessageProperties messageProperties = rabbitMessage.getMessageProperties();

        // MQ消息，附加头部信息
        Message mqMessage = new Message(messageBody.getBytes(StandardCharsets.UTF_8), messageProperties);

        // 关联数据，添加消息进去，用于发送确认失败后处理
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        correlationData.setReturnedMessage(mqMessage);

        this.delegate.getRabbitTemplate().send(exchange, routingKey, mqMessage, correlationData);
    }

    /**
     * 发送消息
     *
     * @param rabbitMessage 消息
     */
    public void send(RabbitMessage rabbitMessage) {
        this.send(this.delegate.getExchange(), this.delegate.getRoutingKey(), rabbitMessage);
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
