package com.mylar.lib.rabbitmq.component.register;

import com.mylar.lib.rabbitmq.component.core.IRabbitReceiver;
import com.mylar.lib.rabbitmq.component.data.RabbitConnectionKey;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.component.constant.RabbitConstant;
import com.mylar.lib.rabbitmq.component.utils.RabbitConnectionFactoryUtil;
import com.mylar.lib.rabbitmq.component.utils.RabbitHostNameUtil;
import com.mylar.lib.rabbitmq.component.utils.RabbitPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * RabbitMQ消费者注册类
 *
 * @author wangz
 * @date 2021/11/10 0010 23:16
 */
public class RabbitReceiverRegister extends AbstractRabbitRegister {

    /**
     * 构造方法
     *
     * @param environment        环境配置
     * @param applicationContext 应用上下文
     */
    public RabbitReceiverRegister(Environment environment, ApplicationContext applicationContext) {
        super(environment, applicationContext);
        this.listenerContainers = new ArrayList<>();
    }

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(RabbitReceiverRegister.class);

    /**
     * 监听容器集合
     */
    List<SimpleMessageListenerContainer> listenerContainers;

    /**
     * 注册
     */
    @Override
    public void register() {

        // 获取所有消费者实例
        Map<String, IRabbitReceiver> allReceivers = this.applicationContext.getBeansOfType(IRabbitReceiver.class);
        if (allReceivers.size() == 0) {
            return;
        }

        for (IRabbitReceiver receiver : allReceivers.values()) {

            // 获取注解
            Class<?> clazz = AopUtils.getTargetClass(receiver);
            RabbitParameter rabbitParameter = AnnotationUtils.findAnnotation(clazz, RabbitParameter.class);
            if (rabbitParameter == null) {
                continue;
            }

            // 获取监听容器
            SimpleMessageListenerContainer listenerContainer = this.getListenerContainer(rabbitParameter, receiver);

            // 启动
            listenerContainer.start();

            // 加入到集合，用于销毁
            this.listenerContainers.add(listenerContainer);
        }
    }

    /**
     * 停止监听容器
     */
    public void stopListeners() {
        this.listenerContainers.forEach(AbstractMessageListenerContainer::stop);
    }

    /**
     * 获取监听容器
     *
     * @param rabbitParameter 注解参数
     * @param receiver        消费者实例
     * @return 监听容器
     */
    private SimpleMessageListenerContainer getListenerContainer(RabbitParameter rabbitParameter, IRabbitReceiver receiver) {

        // 配置前缀
        String prefix = rabbitParameter.prefix();

        // 配置属性：优先取缓存
        RabbitProperties rabbitProperties = this.rabbitElementCache.getRabbitProperties(prefix);
        if (rabbitProperties == null) {
            rabbitProperties = RabbitPropertiesUtil.dataBind(environment, prefix, RabbitProperties.class);
            this.rabbitElementCache.setRabbitProperties(prefix, rabbitProperties);
        }

        // 生产者连接池：优先取缓存
        RabbitConnectionKey connectionKey = new RabbitConnectionKey(prefix, RabbitConstant.SCOPE_RECEIVER);
        ConnectionFactory connectionFactory = this.rabbitElementCache.getConnectionFactory(connectionKey);
        if (connectionFactory == null) {
            connectionFactory = RabbitConnectionFactoryUtil.getCachingConnectionFactory(rabbitProperties, connectionKey.getScope());
            this.rabbitElementCache.setConnectionFactory(connectionKey, connectionFactory);
        }

        // 监听容器
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        // 读取配置属性
        RabbitProperties.SimpleContainer simpleContainer = rabbitProperties.getListener().getSimple();

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

        // 设置消费者数量
        this.setConsumers(rabbitParameter, container);

        // 确认模式：手动
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        // 消费者标签策略
        container.setConsumerTagStrategy(s -> String.format("%s-[%s]",
                RabbitHostNameUtil.HOSTNAME != null ? RabbitHostNameUtil.HOSTNAME : "", UUID.randomUUID()));

        // 交换机
        Exchange exchange = ExchangeBuilder.directExchange(rabbitParameter.exchange()).durable(true).build();

        // 设置队列
        Queue queue = QueueBuilder.durable(rabbitParameter.queue()).build();
        container.setQueues(queue);

        // 设置监听者
        container.setMessageListener(receiver);

        // 绑定队列
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareExchange(exchange);
        admin.declareQueue(queue);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(rabbitParameter.routingKey()).noargs());
        admin.afterPropertiesSet();
        container.setAmqpAdmin(admin);
        return container;
    }

    /**
     * 设置消费者数量
     *
     * @param rabbitParameter   注解参数
     * @param listenerContainer 监听容器
     */
    private void setConsumers(RabbitParameter rabbitParameter, SimpleMessageListenerContainer listenerContainer) {

        if (!StringUtils.isEmpty(rabbitParameter.concurrency())) {
            listenerContainer.setConcurrentConsumers(Integer.parseInt(rabbitParameter.concurrency()));
        }

        if (!StringUtils.isEmpty(rabbitParameter.maxConcurrency())) {
            listenerContainer.setMaxConcurrentConsumers(Integer.parseInt(rabbitParameter.maxConcurrency()));
        }

        if (!StringUtils.isEmpty(rabbitParameter.prefetch())) {
            listenerContainer.setPrefetchCount(Integer.parseInt(rabbitParameter.prefetch()));
        }
    }
}
