package com.mylar.lib.rabbitmq.test.plugins;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.test.wrapper.FailedRetryRabbitSender;
import com.mylar.lib.rabbitmq.test.wrapper.MyConstant;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Objects;

/**
 * 消息处理
 *
 * @author wangz
 * @date 2021/11/11 0011 0:14
 */
@RabbitParameter(
        prefix = MyConstant.PREFIX_MY,
        exchange = "exchange.x",
        routingKey = "routing.key.x",
        queue = "queue.x"
)
public class TestRetryRabbitHandler extends AbstractRabbitSender implements IRabbitReceiver {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(TestRetryRabbitHandler.class);

    /**
     * 生产者-失败重试
     */
    @Autowired
    private FailedRetryRabbitSender failedRetryRabbitSender;

    /**
     * 消息处理
     *
     * @param message 消息
     * @param channel 信道
     * @throws IOException 异常
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String messageBody = new String(message.getBody());

        // 消息处理失败，发送消息到失败重试队列
        if (Objects.equals(messageBody, "failed")) {
            logger.error(String.format("message handle failed, send to retry queue, message: %s", messageBody));
            this.failedRetryRabbitSender.transmit(message);
        }
        // 消息处理成功
        else {
            logger.info(String.format("message handle successfully, message: %s", messageBody));
        }

        // ACK
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
