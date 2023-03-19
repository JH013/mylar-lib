package com.mylar.lib.job.single.annotation;

import org.quartz.listeners.JobListenerSupport;
import org.quartz.listeners.TriggerListenerSupport;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单机任务基础配置
 *
 * @author wangz
 * @date 2023/3/8 0008 22:49
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface SingleJobBasicConfig {

    /**
     * 任务名
     *
     * @return 任务名
     */
    String jobName();

    /**
     * 任务所属组
     *
     * @return 任务所属组
     */
    String jobGroup() default "default";

    /**
     * cron 表达式，工具：https://cron.qqe2.com/
     *
     * @return cron 表达式
     */
    String cron();

    /**
     * 任务监听器集合
     *
     * @return 任务监听器集合
     */
    Class<? extends JobListenerSupport>[] jobListeners() default {};


    /**
     * 任务触发器监听器集合
     *
     * @return 任务触发器监听器集合
     */
    Class<? extends TriggerListenerSupport>[] triggerListeners() default {};
}
