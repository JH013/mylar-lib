package com.mylar.sample.modules.job.control.annotation;

import com.mylar.lib.job.single.annotation.SingleJobControlConfig;
import com.mylar.sample.modules.job.control.TimeRangeJobControl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务控制配置-运行时间区间
 *
 * @author wangz
 * @date 2023/3/12 0012 18:19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SingleJobControlConfig(controls = TimeRangeJobControl.class)
public @interface TimeRangeJobControlConfig {

    /**
     * 运行时间区间
     *
     * @return 运行时间区间
     */
    String[] timeSpansToRun();
}
