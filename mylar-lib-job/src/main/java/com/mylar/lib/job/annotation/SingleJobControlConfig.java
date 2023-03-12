package com.mylar.lib.job.annotation;

import com.mylar.lib.job.control.AbstractSingleJobControl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单机任务控制配置
 *
 * @author wangz
 * @date 2023/3/11 0011 18:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleJobControlConfig {

    /**
     * 任务控制集合
     *
     * @return 任务控制集合
     */
    Class<? extends AbstractSingleJobControl>[] controls();
}