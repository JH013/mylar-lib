package com.mylar.sample.modules.job.control.annotation;

import com.mylar.lib.job.single.annotation.SingleJobControlConfig;
import com.mylar.sample.modules.job.control.ConfigurableCronJobControl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务控制配置-可配置 cron
 *
 * @author wangz
 * @date 2023/3/11 0011 19:50
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SingleJobControlConfig(controls = ConfigurableCronJobControl.class)
public @interface ConfigurableCronJobControlConfig {
}
