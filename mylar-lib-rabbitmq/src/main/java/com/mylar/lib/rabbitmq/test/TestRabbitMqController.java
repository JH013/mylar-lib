package com.mylar.lib.rabbitmq.test;

import com.mylar.lib.rabbitmq.test.plugins.DemoRabbitHandler;
import com.mylar.lib.rabbitmq.test.plugins.DemoRabbitHandler2;
import com.mylar.lib.rabbitmq.test.plugins.DemoRabbitHandler3;
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
    private DemoRabbitHandler demoRabbitHandler;

    @Autowired
    private DemoRabbitHandler2 demoRabbitHandler2;

    @Autowired
    private DemoRabbitHandler3 demoRabbitHandler3;

    @RequestMapping(value = "/send")
    public String send(String memberName, String message) throws Exception {

        this.demoRabbitHandler.send(memberName, message);
        return "success";
    }

    @RequestMapping(value = "/send2")
    public String send2(String memberName, String message) throws Exception {

        this.demoRabbitHandler2.send(memberName, message);
        return "success";
    }

    @RequestMapping(value = "/send3")
    public String send3(String memberName, String message) throws Exception {

        this.demoRabbitHandler3.send(memberName, message);
        return "success";
    }
}
