package com.mylar.lib.rabbitmq.component.configuration;

import com.mylar.lib.rabbitmq.component.register.RabbitReceiverRegister;
import com.mylar.lib.rabbitmq.component.register.RabbitSenderRegister;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 启动类
 *
 * @author wangz
 * @date 2021/11/10 0010 21:11
 */
@Component
public class RabbitStarter implements EnvironmentAware, ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    /**
     * 环境配置
     */
    private Environment environment;

    /**
     * 应用上下文
     */
    private ApplicationContext applicationContext;

    /**
     * RabbitMQ生产者注册实例
     */
    private RabbitSenderRegister rabbitSenderRegister;

    /**
     * RabbitMQ消费者注册实例
     */
    private RabbitReceiverRegister rabbitReceiverRegister;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {

        // 注册生产者
        this.rabbitSenderRegister = new RabbitSenderRegister(this.environment, this.applicationContext);
        this.rabbitSenderRegister.register();

        // 注册消费者
        this.rabbitReceiverRegister = new RabbitReceiverRegister(this.environment, this.applicationContext);
        this.rabbitReceiverRegister.register();
    }

    @Override
    public void destroy() throws Exception {

        // 停止监听
        this.rabbitReceiverRegister.stopListeners();
    }
}
