package com.mylar.lib.thread.pool.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池基础配置
 *
 * @author wangz
 * @date 2023/3/21 0021 21:20
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ThreadPoolBasicConfig {

    /**
     * 线程池唯一键
     *
     * @return 线程池唯一键
     */
    String key();

    /**
     * 线程池描述
     *
     * @return 线程池描述
     */
    String description();

    /**
     * 核心线程池大小
     *
     * @return 核心线程池大小
     */
    int corePoolSize();

    /**
     * 线程池最大容量
     *
     * @return 线程池最大容量
     */
    int maximumPoolSize();

    /**
     * 空闲线程的存活时间
     *
     * @return 空闲线程的存活时间
     */
    long keepAliveTime() default 0L;

    /**
     * 空闲线程的存活时间单位
     *
     * @return 空闲线程的存活时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 阻塞队列容量
     *
     * @return 阻塞队列容量
     */
    int blockingQueueCapacity() default 0;

    /**
     * 拒绝策略
     *
     * @return 拒绝策略
     */
    Class<? extends RejectedExecutionHandler> rejectPolicy() default ThreadPoolExecutor.AbortPolicy.class;
}
