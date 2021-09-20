package com.mylar.lib.rabbitmq.simple;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangz
 * @date 2021/9/18 0018 0:10
 */
@Configuration
public class RabbitMqConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(rabbitTemplate);
    }

}
