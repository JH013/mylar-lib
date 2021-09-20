package com.mylar.lib.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author wangz
 * @date 2021/9/18 0018 0:25
 */
@Service
public class RabbitMqReceiver {

    @RabbitListener(queues = {RabbitMqConstant.queue})
    public void receive(Message message, Channel channel) {
        System.out.println("message: " + message);
    }
}
