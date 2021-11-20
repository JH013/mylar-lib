package com.mylar.lib.rabbitmq.simple.core.constant;

/**
 * 常量
 *
 * @author wangz
 * @date 2021/11/7 0007 21:28
 */
public class SimpleRabbitMqConstant {

    /**
     * MQ连接配置前缀
     */
    public final static String CONNECTION_CONFIG_PREFIX = "simple";

    /**
     * 连接范围：生产者 P 接收者 L
     */
    public final static String CONNECTION_SCOPE_SENDER = "P";
    public final static String CONNECTION_SCOPE_RECEIVER = "L";

    /**
     * bean
     */
    public final static String BEAN_NAME_SENDER = "SimpleRabbitMqSender";
    public final static String BEAN_NAME_RECEIVER = "SimpleRabbitMqReceiver";

    /**
     * 消息头KEY：会员名
     */
    public final static String HEADER_KEY_MEMBER_NAME = "MEMBER_NAME";

    /**
     * 普通队列常量
     */
    public final static String EXCHANGE_SIMPLE = "simple.exchange";
    public final static String ROUTING_KEY_SIMPLE = "simple.routing.key";
    public final static String QUEUE_SIMPLE = "simple.queue";

    /**
     * 延时队列常量
     */
    public final static String EXCHANGE_SIMPLE_DELAY = "simple.exchange.delay";
    public final static String ROUTING_KEY_SIMPLE_DELAY = "simple.routing.key.delay";
    public final static String QUEUE_SIMPLE_DELAY = "simple.queue.delay";
    public final static String DLX_EXCHANGE_SIMPLE = "dlx.simple.exchange";
    public final static String DLX_ROUTING_KEY_PREFIX = "dlx.simple.routing.key.";
    public final static String DLX_QUEUE_PREFIX = "dlx.simple.queue.";

}
