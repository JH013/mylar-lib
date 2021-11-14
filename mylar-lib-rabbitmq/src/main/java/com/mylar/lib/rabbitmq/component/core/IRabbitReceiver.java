package com.mylar.lib.rabbitmq.component.core;

import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

/**
 * RabbitMQ消费者接口
 *
 * @author wangz
 * @date 2021/11/10 0010 20:29
 */
public interface IRabbitReceiver extends ChannelAwareMessageListener {
}
