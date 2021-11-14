package com.mylar.lib.rabbitmq.component.core;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * RabbitMQ参数
 *
 * @author wangz
 * @date 2021/11/10 0010 20:29
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RabbitParameter {

    /**
     * 配置前缀
     *
     * @return 配置前缀
     */
    String prefix();

    /**
     * 交换机
     *
     * @return 交换机
     */
    String exchange() default "";

    /**
     * 路由键
     *
     * @return 路由键
     */
    String routingKey() default "";

    /**
     * 队列名
     *
     * @return 队列名
     */
    String queue() default "";

    /**
     * 消费者数量
     *
     * @return 消费者数量
     */
    String concurrency() default "";

    /**
     * 最大消费者数量
     *
     * @return 最大消费者数量
     */
    String maxConcurrency() default "";

    /**
     * 预处理数量
     *
     * @return 预处理数量
     */
    String prefetch() default "";
}
