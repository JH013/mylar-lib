package com.mylar.lib.job.scheduler;

import org.quartz.Scheduler;

/**
 * 单机任务调度器接口
 *
 * @author wangz
 * @date 2023/3/9 0009 23:38
 */
public interface ISingleJobScheduler {

    /**
     * 获取任务调度器
     *
     * @return 任务调度器
     */
    Scheduler getScheduler();
}
