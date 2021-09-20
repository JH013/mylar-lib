package com.mylar.lib.rabbitmq.simple;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangz
 * @date 2021/9/18 0018 0:32
 */
@RestController
@RequestMapping(value = "/rabbit")
public class RabbitMqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @RequestMapping(value = "/send")
    public String save(String message) throws Exception {
        this.rabbitTemplate.convertAndSend(RabbitMqConstant.exchange, RabbitMqConstant.routingKey, message);
        return "success";
    }
}
