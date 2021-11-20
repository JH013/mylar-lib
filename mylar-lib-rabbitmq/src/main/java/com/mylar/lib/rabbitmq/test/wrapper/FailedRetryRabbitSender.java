package com.mylar.lib.rabbitmq.test.wrapper;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.component.data.RabbitMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

/**
 * 生产者-失败重试
 *
 * @author wangz
 * @date 2021/11/14 0014 23:18
 */
@RabbitParameter(prefix = MyConstant.PREFIX_MY)
public class FailedRetryRabbitSender extends AbstractRabbitSender {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(FailedRetryRabbitSender.class);

    /**
     * 转发消息
     *
     * @param message 消息
     */
    public void transmit(Message message) {

        // 从Header中获取已重试次数，超过最大重试次数停止重试
        Object hasRetryTime = message.getMessageProperties().getHeader(MyConstant.HEADER_RETRY_TIME);
        if (hasRetryTime != null && (int) hasRetryTime >= MyConstant.MAX_RETRY_TIME) {
            logger.error(String.format("Handle failed message, has retry time %s, overdue max retry time %s, stop retry, message: %s",
                    hasRetryTime, MyConstant.MAX_RETRY_TIME, message.toString()));
            return;
        }

        // 消息体
        String messageBody = new String(message.getBody());
        RabbitMessage transmitMessage = new RabbitMessage(messageBody);

        // 消息头：源交换机、源路由键、重试次数
        transmitMessage.setHeader(MyConstant.HEADER_SOURCE_EXCHANGE, message.getMessageProperties().getReceivedExchange());
        transmitMessage.setHeader(MyConstant.HEADER_SOURCE_ROUTING_KEY, message.getMessageProperties().getReceivedRoutingKey());
        transmitMessage.setHeader(MyConstant.HEADER_RETRY_TIME, hasRetryTime == null ? 1 : (int) hasRetryTime + 1);

        // 发送消息到死信队列
        this.send("dlx.exchange.delay", "dlx.routing.key.delay.10000", transmitMessage);
    }

}
