package com.mylar.lib.rabbitmq.component.core;

import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

/**
 * @author wangz
 * @date 2021/11/10 0010 20:29
 */
public interface IRabbitReceiver extends ChannelAwareMessageListener {
}
