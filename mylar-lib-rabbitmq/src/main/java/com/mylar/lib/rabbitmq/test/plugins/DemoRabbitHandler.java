package com.mylar.lib.rabbitmq.test.plugins;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * @author wangz
 * @date 2021/11/11 0011 0:14
 */
@RabbitParameter(
        prefix = "my",
        exchange = "test-exchange",
        routingKey = "test-routing-key",
        queue = "test-queue"
)
public class DemoRabbitHandler extends AbstractRabbitSender implements IRabbitReceiver {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println(message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
