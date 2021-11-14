package com.mylar.lib.rabbitmq.test.wrapper;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.component.data.RabbitMessage;
import org.springframework.amqp.core.Message;

/**
 * @author wangz
 * @date 2021/11/14 0014 23:18
 */
@RabbitParameter(prefix = "my")
public class FailedRetryRabbitSender extends AbstractRabbitSender {

    public void sendMessage(Message message) {

        Object hasRetryTime = message.getMessageProperties().getHeader(MyConstant.HEADER_RETRY_TIME);
        if (hasRetryTime != null && (int) hasRetryTime > MyConstant.MAX_RETRY_TIME) {
            System.out.println(String.format("failed message handle, message: %s, retry time: %s, greater than %s, stop.",
                    message, hasRetryTime, MyConstant.MAX_RETRY_TIME));
            return;
        }

        String receivedExchange = message.getMessageProperties().getReceivedExchange();
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();

        String messageBody = new String(message.getBody());
        RabbitMessage transmitMessage = new RabbitMessage(messageBody);
        transmitMessage.setHeader(MyConstant.HEADER_SOURCE_EXCHANGE, receivedExchange);
        transmitMessage.setHeader(MyConstant.HEADER_SOURCE_ROUTING_KEY, receivedRoutingKey);
        transmitMessage.setHeader(MyConstant.HEADER_RETRY_TIME, hasRetryTime == null ? 1 : (int) hasRetryTime + 1);

        this.send("dlx.exchange.delay", "dlx.routing.key.delay.10000", transmitMessage);
    }

}
