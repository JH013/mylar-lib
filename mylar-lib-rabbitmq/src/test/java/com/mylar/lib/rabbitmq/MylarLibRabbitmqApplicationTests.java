package com.mylar.lib.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MylarLibRabbitmqApplicationTests {

    @Test
    void contextLoads() {
    }

//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    private RabbitAdmin rabbitAdmin;
//
//    @Test
//    public void testDeclare() {
//        DirectExchange directExchange = new DirectExchange(RabbitMqConstant.exchange, true, false);
//        rabbitAdmin.declareExchange(directExchange);
//
//        Queue queue = new Queue(RabbitMqConstant.queue, true, false, false);
//        rabbitAdmin.declareQueue(queue);
//
//        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(directExchange).with(RabbitMqConstant.routingKey));
//    }

}
