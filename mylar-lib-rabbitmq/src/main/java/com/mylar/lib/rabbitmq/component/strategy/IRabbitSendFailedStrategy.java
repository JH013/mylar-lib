package com.mylar.lib.rabbitmq.component.strategy;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 发送失败策略接口
 *
 * @author wangz
 * @date 2021/11/10 0010 22:35
 */
public interface IRabbitSendFailedStrategy extends RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

}
