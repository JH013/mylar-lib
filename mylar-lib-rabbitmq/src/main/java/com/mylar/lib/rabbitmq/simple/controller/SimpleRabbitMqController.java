package com.mylar.lib.rabbitmq.simple.controller;

import com.mylar.lib.rabbitmq.simple.core.constant.SimpleRabbitMqConstant;
import com.mylar.lib.rabbitmq.simple.core.SimpleRabbitMqSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangz
 * @date 2021/9/18 0018 0:32
 */
@RestController
@RequestMapping(value = "/rabbit/simple")
public class SimpleRabbitMqController {

    @Autowired
    private SimpleRabbitMqSender simpleRabbitMqSender;

    @RequestMapping(value = "/send/direct")
    public String save(String message) throws Exception {

        this.simpleRabbitMqSender.send(
                SimpleRabbitMqConstant.EXCHANGE_SIMPLE,
                SimpleRabbitMqConstant.ROUTING_KEY_SIMPLE,
                message
        );

        return "success";
    }

    @RequestMapping(value = "/send/delay")
    public String save(String message, String delay) throws Exception {

        this.simpleRabbitMqSender.send(
                SimpleRabbitMqConstant.DLX_EXCHANGE_SIMPLE,
                SimpleRabbitMqConstant.DLX_ROUTING_KEY_PREFIX + delay,
                message
        );

        return "success";
    }

    @RequestMapping(value = "/send/error")
    public String save(String exchange, String routingKey, String message) throws Exception {

        this.simpleRabbitMqSender.send(
                exchange,
                routingKey,
                message
        );

        return "success";
    }
}
