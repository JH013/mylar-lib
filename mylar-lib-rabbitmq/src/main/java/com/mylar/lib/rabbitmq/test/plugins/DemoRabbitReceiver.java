package com.mylar.lib.rabbitmq.test.plugins;

import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * @author wangz
 * @date 2021/11/13 0013 20:36
 */
@RabbitParameter(
        prefix = "my",
        exchange = "exchange.demo",
        routingKey = "routing.demo",
        queue = "queue.demo"
)
public class DemoRabbitReceiver implements IRabbitReceiver {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println(message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
