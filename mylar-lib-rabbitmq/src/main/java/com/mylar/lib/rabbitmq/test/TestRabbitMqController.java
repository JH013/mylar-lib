package com.mylar.lib.rabbitmq.test;

import com.mylar.lib.rabbitmq.component.data.RabbitMessage;
import com.mylar.lib.rabbitmq.test.plugins.DemoRabbitSender;
import com.mylar.lib.rabbitmq.test.plugins.MyRabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangz
 * @date 2021/11/11 0011 0:13
 */
@RestController
@RequestMapping(value = "/rabbit/test")
public class TestRabbitMqController {

    @Autowired
    private MyRabbitHandler myRabbitHandler;

    @Autowired
    private DemoRabbitSender demoRabbitSender;

    @RequestMapping(value = "/sendMy")
    public String sendMy(String message) throws Exception {

        this.myRabbitHandler.send(new RabbitMessage(message));
        return "success";
    }

    @RequestMapping(value = "/sendDemo")
    public String sendDemo(String message) throws Exception {

        this.demoRabbitSender.send(new RabbitMessage(message));
        return "success";
    }

}
