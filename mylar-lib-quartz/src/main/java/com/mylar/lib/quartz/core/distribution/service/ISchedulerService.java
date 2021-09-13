package com.mylar.lib.quartz.core.distribution.service;

import org.quartz.SchedulerException;

/**
 * 调度服务接口
 *
 * @author wangz
 * @date 2021/9/12 0012 23:22
 */
public interface ISchedulerService {

    /**
     * 移除任务
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @throws SchedulerException 异常
     */
    void removeJob
    (
            String jobName,
            String jobGroupName,
            String triggerName,
            String triggerGroupName
    ) throws SchedulerException;

    /**
     * 修改任务时间
     *
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组名称
     * @param cron             cron
     * @throws SchedulerException 异常
     */
    void modifyJobTime
    (
            String triggerName,
            String triggerGroupName,
            String cron
    ) throws SchedulerException;

    /**
     * 暂停任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名称
     * @throws SchedulerException 异常
     */
    void pauseJob(String jobName, String jobGroupName) throws SchedulerException;

    /**
     * 重启任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名称
     * @throws SchedulerException 异常
     */
    void resumeJob(String jobName, String jobGroupName) throws SchedulerException;
}
