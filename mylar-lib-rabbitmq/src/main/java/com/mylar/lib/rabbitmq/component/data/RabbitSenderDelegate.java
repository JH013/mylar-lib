package com.mylar.lib.rabbitmq.component.data;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * RabbitMQ发送者委托
 *
 * @author wangz
 * @date 2021/11/10 0010 20:58
 */
public class RabbitSenderDelegate {

    /**
     * 交换机
     */
    private String exchange;

    /**
     * 路由键
     */
    private String routingKey;

    /**
     * RabbitTemplate
     */
    private RabbitTemplate rabbitTemplate;

    // region getter & setter

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

    // endregion
}
