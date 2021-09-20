package com.mylar.lib.rabbitmq;

import com.mylar.lib.rabbitmq.simple.RabbitMqConstant;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MylarLibRabbitmqApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    public void testDeclare() {
        DirectExchange directExchange = new DirectExchange(RabbitMqConstant.exchange, true, false);
        rabbitAdmin.declareExchange(directExchange);

        Queue queue = new Queue(RabbitMqConstant.queue, true, false, false);
        rabbitAdmin.declareQueue(queue);

        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(RabbitMqConstant.routingKey));
    }

}
