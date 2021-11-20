package com.mylar.lib.rabbitmq.test.plugins;

import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.test.wrapper.MyConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * Demo Receiver
 * <p>
 * 1、声明消费者
 * 2、RabbitParameter需要指定交换机、路由键、队列
 *
 * @author wangz
 * @date 2021/11/13 0013 20:36
 */
@RabbitParameter(
        prefix = MyConstant.PREFIX_MY,
        exchange = "exchange.demo",
        routingKey = "routing.demo",
        queue = "queue.demo"
)
public class DemoRabbitReceiver implements IRabbitReceiver {

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
