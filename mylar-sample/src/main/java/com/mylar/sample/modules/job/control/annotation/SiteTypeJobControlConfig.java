package com.mylar.sample.modules.job.control.annotation;

import com.mylar.lib.job.annotation.SingleJobControlConfig;
import com.mylar.sample.modules.job.control.SiteTypeJobControl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务控制配置-运行站点
 *
 * @author wangz
 * @date 2023/3/12 0012 16:02
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SingleJobControlConfig(controls = SiteTypeJobControl.class)
public @interface SiteTypeJobControlConfig {

    /**
     * 运行站点
     *
     * @return 运行站点
     */
    String[] sitesToRun();
}
