package com.mylar.lib.job.control;

import org.quartz.JobKey;

/**
 * 单机任务控制聚合
 *
 * @author wangz
 * @date 2023/3/12 0012 5:38
 */
public interface ISingleJobControlAggregation {

    /**
     * 是否初始化
     *
     * @param jobKey 任务键
     * @return 是否初始化
     */
    boolean enableInit(JobKey jobKey);

    /**
     * 获取 cron
     *
     * @param jobKey 任务键
     * @return cron
     */
    String getCron(JobKey jobKey);

    /**
     * 当前是否可执行
     *
     * @param jobKey 任务键
     * @return 是否可执行
     */
    boolean enableRunCurrent(JobKey jobKey);
}
