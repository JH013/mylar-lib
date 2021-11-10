package com.mylar.lib.rabbitmq.component.register;

import com.mylar.lib.rabbitmq.component.core.AbstractRabbitSender;
import com.mylar.lib.rabbitmq.component.core.RabbitConnectionKey;
import com.mylar.lib.rabbitmq.component.core.RabbitParameter;
import com.mylar.lib.rabbitmq.component.core.RabbitSenderDelegate;
import com.mylar.lib.rabbitmq.component.constant.RabbitConstant;
import com.mylar.lib.rabbitmq.component.strategy.DefaultRabbitSendFailedStrategy;
import com.mylar.lib.rabbitmq.component.strategy.IRabbitSendFailedStrategy;
import com.mylar.lib.rabbitmq.component.utils.RabbitConnectionFactoryUtil;
import com.mylar.lib.rabbitmq.component.utils.RabbitPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * RabbitMQ生产者注册类
 *
 * @author wangz
 * @date 2021/11/10 0010 22:51
 */
public class RabbitSenderRegister extends AbstractRabbitRegister {

    /**
     * 构造方法
     *
     * @param environment        环境配置
     * @param applicationContext 应用上下文
     */
    public RabbitSenderRegister(Environment environment, ApplicationContext applicationContext) {
        super(environment, applicationContext);
    }

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(RabbitSenderRegister.class);

    /**
     * 注册
     */
    @Override
    public void register() {

        // 获取所有生产者实例
        Map<String, AbstractRabbitSender> allSenders = this.applicationContext.getBeansOfType(AbstractRabbitSender.class);
        if (allSenders.size() == 0) {
            return;
        }

        for (AbstractRabbitSender sender : allSenders.values()) {

            // 获取注解
            Class<?> clazz = AopUtils.getTargetClass(sender);
            RabbitParameter rabbitParameter = AnnotationUtils.findAnnotation(clazz, RabbitParameter.class);
            if (rabbitParameter == null) {
                continue;
            }

            // 获取RabbitTemplate
            RabbitTemplate rabbitTemplate = this.getRabbitTemplate(rabbitParameter);

            // 新建生产者委托
            RabbitSenderDelegate senderDelegate = new RabbitSenderDelegate();
            senderDelegate.setExchange(rabbitParameter.exchange());
            senderDelegate.setRoutingKey(rabbitParameter.routingKey());
            senderDelegate.setRabbitTemplate(rabbitTemplate);
            sender.setDelegate(senderDelegate);
        }
    }

    /**
     * 获取RabbitTemplate
     *
     * @param rabbitParameter 注解参数
     * @return RabbitTemplate
     */
    private RabbitTemplate getRabbitTemplate(RabbitParameter rabbitParameter) {

        // 配置前缀
        String prefix = rabbitParameter.prefix();

        // 配置属性：优先取缓存
        RabbitProperties rabbitProperties = this.rabbitElementCache.getRabbitProperties(prefix);
        if (rabbitProperties == null) {
            rabbitProperties = RabbitPropertiesUtil.dataBind(environment, prefix, RabbitProperties.class);
            this.rabbitElementCache.setRabbitProperties(prefix, rabbitProperties);
        }

        // 生产者连接池：优先取缓存
        RabbitConnectionKey connectionKey = new RabbitConnectionKey(prefix, RabbitConstant.SCOPE_SENDER);
        ConnectionFactory connectionFactory = this.rabbitElementCache.getConnectionFactory(connectionKey);
        if (connectionFactory == null) {
            connectionFactory = RabbitConnectionFactoryUtil.getCachingConnectionFactory(rabbitProperties, connectionKey.getScope());
            this.rabbitElementCache.setConnectionFactory(connectionKey, connectionFactory);
        }

        // RabbitTemplate：优先取缓存
        RabbitTemplate rabbitTemplate = this.rabbitElementCache.getRabbitTemplate(prefix);
        if (rabbitTemplate == null) {
            rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setMandatory(true);

            // 发送失败策略：优先取缓存
            IRabbitSendFailedStrategy sendFailedStrategy = this.getSendFailedStrategy(rabbitParameter.sendFailedStrategy());
            rabbitTemplate.setConfirmCallback(sendFailedStrategy);
            rabbitTemplate.setReturnsCallback(sendFailedStrategy);
            this.rabbitElementCache.setRabbitTemplate(prefix, rabbitTemplate);
        }

        return rabbitTemplate;
    }

    /**
     * 获取发送失败策略
     *
     * @param clazz 类型
     * @return 发送失败策略实例
     */
    private IRabbitSendFailedStrategy getSendFailedStrategy(Class<? extends IRabbitSendFailedStrategy> clazz) {
        try {
            IRabbitSendFailedStrategy sendFailedStrategy = this.rabbitElementCache.getSendFailedStrategy(clazz);
            if (sendFailedStrategy == null) {
                sendFailedStrategy = clazz.newInstance();
                this.rabbitElementCache.setSendFailedStrategy(clazz, sendFailedStrategy);
            }

            return sendFailedStrategy;
        } catch (Exception e) {
            logger.error("init send failed strategy failed, error message: ", e);
            return new DefaultRabbitSendFailedStrategy();
        }
    }
}
