package com.mylar.lib.rabbitmq.component.core;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author wangz
 * @date 2021/11/10 0010 20:58
 */
public class RabbitSenderDelegate {

    private String exchange;

    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
}
