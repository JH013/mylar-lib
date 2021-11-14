package com.mylar.lib.rabbitmq.test.wrapper;

import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.component.data.RabbitMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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

    @Autowired
    private FailedRetryRabbitSender failedRetryRabbitSender;

    @Override
    public void onMessage(Message message, Channel channel) throws IOException {
        String receivedExchange = message.getMessageProperties().getReceivedExchange();
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();


        System.out.println(String.format("failed retry receive message: %s", message));

        String messageBody = new String(message.getBody());
        RabbitMessage transmitMessage = new RabbitMessage(messageBody);

        String exchange = message.getMessageProperties().getHeader(MyConstant.HEADER_SOURCE_EXCHANGE).toString();
        String routingKey = message.getMessageProperties().getHeader(MyConstant.HEADER_SOURCE_ROUTING_KEY).toString();
        Object hasRetryTime = message.getMessageProperties().getHeader(MyConstant.HEADER_RETRY_TIME);
        transmitMessage.setHeader(MyConstant.HEADER_RETRY_TIME, hasRetryTime);

        this.failedRetryRabbitSender.send(exchange, routingKey, transmitMessage);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
