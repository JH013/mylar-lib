package com.mylar.lib.rabbitmq.simple.core;

import com.mylar.lib.rabbitmq.simple.core.constant.SimpleRabbitMqConstant;
import com.mylar.lib.rabbitmq.simple.core.utils.SimpleRabbitMqConnectionFactoryUtil;
import com.mylar.lib.rabbitmq.simple.core.utils.SimpleRabbitMqHostNameUtil;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * RabbitMQ消费者
 *
 * @author wangz
 * @date 2021/11/7 0007 22:02
 */
@Component(SimpleRabbitMqConstant.BEAN_NAME_RECEIVER)
public class SimpleRabbitMqReceiver {

    /**
     * 构造方法
     */
    public SimpleRabbitMqReceiver() {

    }

    // region 变量

    /**
     * RabbitMQ配置属性
     */
    private RabbitProperties rabbitProperties;

    /**
     * MQ连接
     */
    private ConnectionFactory cachingConnectionFactory;

    // endregion

    // region 公共方法

    /**
     * 初始化
     *
     * @param rabbitProperties RabbitMQ配置属性
     */
    public void init(RabbitProperties rabbitProperties) {

        this.rabbitProperties = rabbitProperties;

        // 创建消费者连接池
        this.cachingConnectionFactory = SimpleRabbitMqConnectionFactoryUtil.getCachingConnectionFactory(rabbitProperties, SimpleRabbitMqConstant.CONNECTION_SCOPE_RECEIVER);

        // 初始化普通直连队列监听
        this.initListener(
                SimpleRabbitMqConstant.EXCHANGE_SIMPLE,
                SimpleRabbitMqConstant.ROUTING_KEY_SIMPLE,
                SimpleRabbitMqConstant.QUEUE_SIMPLE
        );

        // 初始化延时队列监听
        this.initListener(
                SimpleRabbitMqConstant.EXCHANGE_SIMPLE_DELAY,
                SimpleRabbitMqConstant.ROUTING_KEY_SIMPLE_DELAY,
                SimpleRabbitMqConstant.QUEUE_SIMPLE_DELAY
        );
    }

    // endregion

    // region 私有方法

    /**
     * 初始化监听
     *
     * @param exchangeName   交换机名称
     * @param routingKeyName 路由键名称
     * @param queueName      队列名称
     */
    private void initListener(String exchangeName, String routingKeyName, String queueName) {

        // 监听容器
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(this.cachingConnectionFactory);

        // 读取配置属性
        RabbitProperties.SimpleContainer simpleContainer = this.rabbitProperties.getListener().getSimple();

        // 消费者数量
        if (simpleContainer.getConcurrency() != null) {
            container.setConcurrentConsumers(simpleContainer.getConcurrency());
        }

        // 最大消费者数量
        if (simpleContainer.getMaxConcurrency() != null) {
            container.setMaxConcurrentConsumers(simpleContainer.getMaxConcurrency());
        }

        // 预处理数量
        if (simpleContainer.getPrefetch() != null) {
            container.setPrefetchCount(simpleContainer.getPrefetch());
        }

        // 确认模式：手动
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        // 消费者标签策略
        container.setConsumerTagStrategy(s -> String.format("%s-[%s]",
                SimpleRabbitMqHostNameUtil.HOSTNAME != null ? SimpleRabbitMqHostNameUtil.HOSTNAME : "", UUID.randomUUID()));

        // 交换机
        Exchange exchange = ExchangeBuilder.directExchange(exchangeName).durable(true).build();

        // 设置队列
        Queue queue = QueueBuilder.durable(queueName).build();
        container.setQueues(queue);

        // 设置监听者
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            System.out.println(message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        });

        // 绑定队列
        RabbitAdmin admin = new RabbitAdmin(cachingConnectionFactory);
        admin.declareExchange(exchange);
        admin.declareQueue(queue);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKeyName).noargs());
        admin.afterPropertiesSet();
        container.setAmqpAdmin(admin);

        // 启动监听容器
        container.start();
    }

    // endregion
}
