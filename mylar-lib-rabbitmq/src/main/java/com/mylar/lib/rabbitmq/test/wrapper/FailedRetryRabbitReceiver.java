package com.mylar.lib.rabbitmq.test.wrapper;

import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

/**
 * @author wangz
 * @date 2021/11/14 0014 23:09
 */
@RabbitParameter(
        prefix = "my",
        exchange = "exchange.retry",
        routingKey = "routing.key.retry",
        queue = "queue.retry"
)
public class FailedRetryRabbitReceiver implements IRabbitReceiver {

    @Override
    public void onMessage(Message message, Channel channel) {

    }
}
