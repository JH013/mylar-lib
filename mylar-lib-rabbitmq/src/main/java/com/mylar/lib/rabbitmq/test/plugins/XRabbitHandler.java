package com.mylar.lib.rabbitmq.test.plugins;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.test.wrapper.FailedRetryRabbitSender;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * @author wangz
 * @date 2021/11/11 0011 0:14
 */
@RabbitParameter(
        prefix = "my",
        exchange = "exchange.x",
        routingKey = "routing.key.x",
        queue = "queue.x"
)
public class XRabbitHandler extends AbstractRabbitSender implements IRabbitReceiver {

    @Autowired
    private FailedRetryRabbitSender failedRetryRabbitSender;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String messageBody = new String(message.getBody());

        if (Objects.equals(messageBody, "failed")) {
            System.out.println(String.format("x handler receive message: %s, process failed, send to retry queue.", message));
            this.failedRetryRabbitSender.sendMessage(message);
        } else {
            System.out.println(String.format("x handler receive message: %s, process success.", message));
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
