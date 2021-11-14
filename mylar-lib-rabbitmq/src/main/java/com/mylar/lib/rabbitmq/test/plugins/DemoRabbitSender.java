package com.mylar.lib.rabbitmq.test.plugins;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;

/**
 * @author wangz
 * @date 2021/11/13 0013 20:39
 */
@RabbitParameter(
        prefix = "my",
        exchange = "exchange.demo",
        routingKey = "routing.demo",
        queue = "queue.demo"
)
public class DemoRabbitSender extends AbstractRabbitSender {

}