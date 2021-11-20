package com.mylar.lib.rabbitmq.test.wrapper;

/**
 * 常量
 *
 * @author wangz
 * @date 2021/11/15 0015 0:18
 */
public class MyConstant {

    /**
     * 配置前缀
     */
    public static final String PREFIX_MY = "my";

    /**
     * 消息头-源交换机
     */
    public static final String HEADER_SOURCE_EXCHANGE = "SYSTEM_HEADER_SOURCE_EXCHANGE";

    /**
     * 消息头-源路由键
     */
    public static final String HEADER_SOURCE_ROUTING_KEY = "SYSTEM_HEADER_SOURCE_ROUTING_KEY";

    /**
     * 消息头-失败重试次数
     */
    public static final String HEADER_RETRY_TIME = "SYSTEM_HEADER_FAILED_RETRY_TIME";

    /**
     * 最大失败重试次数
     */
    public static final int MAX_RETRY_TIME = 3;

}
