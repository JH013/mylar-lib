package com.mylar.lib.rabbitmq.component.configuration;

import com.mylar.lib.rabbitmq.component.constant.RabbitConstant;
import com.mylar.lib.rabbitmq.component.core.IRabbitSenderCallback;
import com.mylar.lib.rabbitmq.component.data.RabbitConnectionKey;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ相关缓存
 *
 * @author wangz
 * @date 2021/11/10 0010 21:15
 */
@Component(RabbitConstant.BEAN_RABBIT_ELEMENT_CACHE)
public class RabbitElementCache {

    /**
     * RabbitMQ属性配置缓存
     */
    private final Map<String, RabbitProperties> rabbitPropertiesCache = new HashMap<>();

    /**
     * RabbitMQ连接池缓存
     */
    private final Map<RabbitConnectionKey, ConnectionFactory> senderConnectionFactoryCache = new HashMap<>();

    /**
     * RabbitTemplate缓存
     */
    private final Map<String, RabbitTemplate> rabbitTemplateCache = new HashMap<>();

    /**
     * RabbitMQ回调缓存
     */
    private final Map<String, IRabbitSenderCallback> rabbitCallbackCache = new HashMap<>();

    // region getter & setter

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

    public IRabbitSenderCallback getRabbitCallback(String prefix) {
        return this.rabbitCallbackCache.get(prefix);
    }

    public void setRabbitCallback(String prefix, IRabbitSenderCallback rabbitCallback) {
        this.rabbitCallbackCache.put(prefix, rabbitCallback);
    }

    // endregion
}
