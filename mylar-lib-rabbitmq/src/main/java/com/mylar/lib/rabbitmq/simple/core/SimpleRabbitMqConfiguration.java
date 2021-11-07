package com.mylar.lib.rabbitmq.simple.core;

import com.mylar.lib.rabbitmq.simple.core.constant.SimpleRabbitMqConstant;
import com.mylar.lib.rabbitmq.simple.core.utils.SimpleRabbitMqPropertiesUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ配置启动类
 *
 * @author wangz
 * @date 2021/11/7 0007 20:44
 */
@Component
public class SimpleRabbitMqConfiguration implements ApplicationContextAware, EnvironmentAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    private Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.initRabbitMq();
    }

    /**
     * 初始化RabbitMQ
     */
    private void initRabbitMq() {

        // 读取配置
        RabbitProperties rabbitProperties = SimpleRabbitMqPropertiesUtil.dataBind(environment, SimpleRabbitMqConstant.CONNECTION_CONFIG_PREFIX, RabbitProperties.class);

        // 初始化生产者
        SimpleRabbitMqSender sender = this.applicationContext.getBean(SimpleRabbitMqConstant.BEAN_NAME_SENDER, SimpleRabbitMqSender.class);
        sender.init(rabbitProperties);

        // 初始化消费者
        SimpleRabbitMqReceiver receiver = this.applicationContext.getBean(SimpleRabbitMqConstant.BEAN_NAME_RECEIVER, SimpleRabbitMqReceiver.class);
        receiver.init(rabbitProperties);
    }
}