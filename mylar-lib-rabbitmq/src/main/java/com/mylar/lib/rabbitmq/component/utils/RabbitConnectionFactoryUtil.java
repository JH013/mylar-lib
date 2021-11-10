package com.mylar.lib.rabbitmq.component.utils;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接工厂工具类
 *
 * @author wangz
 * @date 2021/11/7 0007 22:29
 */
public class RabbitConnectionFactoryUtil {

    /**
     * 连接名称索引，自增
     */
    private static final AtomicInteger INDEX = new AtomicInteger(0);

    /**
     * 创建连接池
     *
     * @param props 配置属性
     * @param scope 范围，区分发送者和生产者
     * @return 连接
     */
    public static ConnectionFactory getCachingConnectionFactory(RabbitProperties props, String scope) {

        com.rabbitmq.client.ConnectionFactory connectionFactory = RabbitConnectionFactoryUtil.getConnectionFactory(props);

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory);
        cachingConnectionFactory.setConnectionNameStrategy(cf -> {
            String name = RabbitHostNameUtil.HOSTNAME;
            if (name == null) {
                name = "SpringAMQP";
            }

            return name + "#" + scope + "-" + INDEX.getAndIncrement();
        });

        cachingConnectionFactory.setAddresses(props.determineAddresses());
        cachingConnectionFactory.setPublisherConfirms(true);
        cachingConnectionFactory.setPublisherReturns(true);
        if (props.getCache().getChannel().getSize() != null) {
            cachingConnectionFactory.setChannelCacheSize(props.getCache().getChannel().getSize());
        }

        if (props.getCache().getConnection().getMode() != null) {
            cachingConnectionFactory.setCacheMode(props.getCache().getConnection().getMode());
        }

        if (props.getCache().getConnection().getSize() != null) {
            cachingConnectionFactory.setConnectionCacheSize(props.getCache().getConnection().getSize());
        }

        if (props.getCache().getChannel().getCheckoutTimeout() != null) {
            cachingConnectionFactory.setChannelCheckoutTimeout(props.getCache().getChannel().getCheckoutTimeout().toMillis());
        }

        return cachingConnectionFactory;
    }

    /**
     * 获取连接
     *
     * @param props 配置属性
     * @return 连接
     */
    private static com.rabbitmq.client.ConnectionFactory getConnectionFactory(RabbitProperties props) {

        RabbitConnectionFactoryBean factoryBean = new RabbitConnectionFactoryBean();
        if (props.determineHost() != null) {
            factoryBean.setHost(props.determineHost());
        }

        factoryBean.setPort(props.determinePort());

        if (props.determineUsername() != null) {
            factoryBean.setUsername(props.determineUsername());
        }

        if (props.determinePassword() != null) {
            factoryBean.setPassword(props.determinePassword());
        }

        if (props.determineVirtualHost() != null) {
            factoryBean.setVirtualHost(props.determineVirtualHost());
        }

        if (props.getRequestedHeartbeat() != null) {
            factoryBean.setRequestedHeartbeat((int) props.getRequestedHeartbeat().getSeconds());
        }

        RabbitProperties.Ssl ssl = props.getSsl();
        if (ssl != null && ssl.getEnabled() != null && ssl.getEnabled()) {
            factoryBean.setUseSSL(true);
            if (ssl.getAlgorithm() != null) {
                factoryBean.setSslAlgorithm(ssl.getAlgorithm());
            }

            factoryBean.setKeyStore(ssl.getKeyStore());
            factoryBean.setKeyStorePassphrase(ssl.getKeyStorePassword());
            factoryBean.setTrustStore(ssl.getTrustStore());
            factoryBean.setTrustStorePassphrase(ssl.getTrustStorePassword());
        }

        if (props.getConnectionTimeout() != null) {
            factoryBean.setConnectionTimeout((int) props.getConnectionTimeout().toMillis());
        }

        com.rabbitmq.client.ConnectionFactory connectionFactory;
        try {
            factoryBean.afterPropertiesSet();
            connectionFactory = factoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return connectionFactory;
    }
}
