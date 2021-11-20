package com.mylar.lib.rabbitmq.test.wrapper;

import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.component.data.RabbitMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 消费者-失败重试
 *
 * @author wangz
 * @date 2021/11/14 0014 23:09
 */
@RabbitParameter(
        prefix = MyConstant.PREFIX_MY,
        exchange = "exchange.retry",
        routingKey = "routing.key.retry",
        queue = "queue.retry"
)
public class FailedRetryRabbitReceiver implements IRabbitReceiver {

    /**
     * 生产者
     */
    @Autowired
    private FailedRetryRabbitSender sender;

    /**
     * 消息处理
     *
     * @param message 消息
     * @param channel 信道
     * @throws IOException 异常
     */
    @Override
    public void onMessage(Message message, Channel channel) throws IOException {

        // 封装消息
        String messageBody = new String(message.getBody());
        RabbitMessage transmitMessage = new RabbitMessage(messageBody);
        transmitMessage.setHeader(MyConstant.HEADER_RETRY_TIME, message.getMessageProperties().getHeader(MyConstant.HEADER_RETRY_TIME));

        // 从消息头中获取源交换机和源路由键
        String exchange = message.getMessageProperties().getHeader(MyConstant.HEADER_SOURCE_EXCHANGE).toString();
        String routingKey = message.getMessageProperties().getHeader(MyConstant.HEADER_SOURCE_ROUTING_KEY).toString();

        // 发送消息到正常队列进行重试
        this.sender.send(exchange, routingKey, transmitMessage);

        // ACK
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
