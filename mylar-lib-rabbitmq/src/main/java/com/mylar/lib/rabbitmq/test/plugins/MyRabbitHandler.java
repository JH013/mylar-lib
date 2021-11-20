package com.mylar.lib.rabbitmq.test.plugins;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.test.wrapper.MyConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * My MQ Handler
 * <p>
 * 1、同时声明生产者和消费者
 *
 * @author wangz
 * @date 2021/11/11 0011 0:14
 */
@RabbitParameter(
        prefix = MyConstant.PREFIX_MY,
        exchange = "exchange.my",
        routingKey = "routing.key.my",
        queue = "queue.my"
)
public class MyRabbitHandler extends AbstractRabbitSender implements IRabbitReceiver {

    /**
     * 消息处理
     *
     * @param message 消息
     * @param channel 信道
     * @throws IOException 异常
     */
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println(message);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
