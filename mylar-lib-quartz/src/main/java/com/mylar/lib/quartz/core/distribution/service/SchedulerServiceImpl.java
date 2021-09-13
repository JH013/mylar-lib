package com.mylar.lib.quartz.core.distribution.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangz
 * @date 2021/9/12 0012 23:22
 */
@Service
public class SchedulerServiceImpl implements ISchedulerService {

    /**
     * 调度器
     */
    @Autowired
    private Scheduler scheduler;

    /**
     * 移除任务
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @throws SchedulerException 异常
     */
    @Override
    public void removeJob
    (
            String jobName,
            String jobGroupName,
            String triggerName,
            String triggerGroupName
    ) throws SchedulerException {

        // 触发器Key
        TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);

        // 停止触发器
        this.scheduler.pauseTrigger(triggerKey);

        // 移除触发器
        this.scheduler.unscheduleJob(triggerKey);

        // 任务Key
        JobKey jobKey = new JobKey(jobName, jobGroupName);

        // 删除任务
        this.scheduler.deleteJob(jobKey);
    }

    /**
     * 修改任务时间
     *
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组名称
     * @param cron             cron
     * @throws SchedulerException 异常
     */
    @Override
    public void modifyJobTime
    (
            String triggerName,
            String triggerGroupName,
            String cron
    ) throws SchedulerException {

        // 触发器Key
        TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);

        // 触发器
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            return;
        }

        // 重置cron
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        this.scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 暂停任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名称
     * @throws SchedulerException 异常
     */
    @Override
    public void pauseJob(String jobName, String jobGroupName) throws SchedulerException {

        // 任务Key
        JobKey jobKey = new JobKey(jobName, jobGroupName);

        // 暂停任务
        this.scheduler.pauseJob(jobKey);
    }

    /**
     * 重启任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名称
     * @throws SchedulerException 异常
     */
    @Override
    public void resumeJob(String jobName, String jobGroupName) throws SchedulerException {

        // 任务Key
        JobKey jobKey = new JobKey(jobName, jobGroupName);

        // 暂停任务
        this.scheduler.resumeJob(jobKey);
    }
}
