package com.mylar.lib.job.distribute.annotation;

import com.mylar.lib.job.distribute.context.IJobContextStrategy;
import com.mylar.lib.job.distribute.sharding.IJobShardingStrategy;
import com.mylar.lib.job.distribute.sharding.plugins.AverageJobShardingStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 简易分布式定时任务配置
 *
 * @author wangz
 * @date 2023/3/19 0019 21:54
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleDistributeJobConfig {

    /**
     * 上下文策略
     *
     * @return 上下文策略
     */
    Class<? extends IJobContextStrategy> jobContextStrategy();

    /**
     * 分片策略
     *
     * @return 分片策略
     */
    Class<? extends IJobShardingStrategy> jobShardingStrategy() default AverageJobShardingStrategy.class;
}
