package com.mylar.lib.rabbitmq.test.plugins;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.test.wrapper.MyConstant;

/**
 * Demo Sender
 * <p>
 * 1、声明生产者
 * 2、如果RabbitParameter不声明交换机和路由键，发送消息时需要指定交换机和路由键
 *
 * @author wangz
 * @date 2021/11/13 0013 20:39
 */
@RabbitParameter(
        prefix = MyConstant.PREFIX_MY,
        exchange = "exchange.demo",
        routingKey = "routing.demo"
)
public class DemoRabbitSender extends AbstractRabbitSender {

}