package com.mylar.lib.rabbitmq.component.core;

import com.mylar.lib.rabbitmq.component.constant.RabbitConstant;
import com.mylar.lib.rabbitmq.component.strategy.IRabbitSendFailedStrategy;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangz
 * @date 2021/11/10 0010 21:15
 */
@Component(RabbitConstant.BEAN_RABBIT_ELEMENT_CACHE)
public class RabbitElementCache {

    private final Map<String, RabbitProperties> rabbitPropertiesCache = new HashMap<>();

    private final Map<RabbitConnectionKey, ConnectionFactory> senderConnectionFactoryCache = new HashMap<>();

    private final Map<String, RabbitTemplate> rabbitTemplateCache = new HashMap<>();

    private final Map<Class<? extends IRabbitSendFailedStrategy>, IRabbitSendFailedStrategy> sendFailedStrategyCache = new HashMap<>();

    public RabbitProperties getRabbitProperties(String prefix) {
        return this.rabbitPropertiesCache.get(prefix);
    }

    public void setRabbitProperties(String prefix, RabbitProperties rabbitProperties) {
        this.rabbitPropertiesCache.put(prefix, rabbitProperties);
    }

    public ConnectionFactory getConnectionFactory(RabbitConnectionKey connectionKey) {
        return this.senderConnectionFactoryCache.get(connectionKey);
    }

    public void setConnectionFactory(RabbitConnectionKey connectionKey, ConnectionFactory connectionFactory) {
        this.senderConnectionFactoryCache.put(connectionKey, connectionFactory);
    }

    public RabbitTemplate getRabbitTemplate(String prefix) {
        return this.rabbitTemplateCache.get(prefix);
    }

    public void setRabbitTemplate(String prefix, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplateCache.put(prefix, rabbitTemplate);
    }

    public IRabbitSendFailedStrategy getSendFailedStrategy(Class<? extends IRabbitSendFailedStrategy> clazz) {
        return this.sendFailedStrategyCache.get(clazz);
    }

    public void setSendFailedStrategy(Class<? extends IRabbitSendFailedStrategy> clazz, IRabbitSendFailedStrategy sendFailedStrategy) {
        this.sendFailedStrategyCache.put(clazz, sendFailedStrategy);
    }

}
