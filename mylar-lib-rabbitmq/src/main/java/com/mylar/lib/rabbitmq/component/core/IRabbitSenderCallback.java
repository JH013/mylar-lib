package com.mylar.lib.rabbitmq.component.core;

import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * RabbitMQ回调接口
 *
 * @author wangz
 * @date 2021/11/14 0014 22:08
 */
public interface IRabbitSenderCallback extends RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback, ChannelCallback<Void> {
}
