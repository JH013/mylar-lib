package com.mylar.lib.quartz.core.distribution.controller;

import com.mylar.lib.quartz.core.distribution.service.ISchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangz
 * @date 2021/9/13 0013 23:09
 */
@RestController
@RequestMapping(value = "/quartz/scheduler")
public class SchedulerController {

    /**
     * 调度服务
     */
    @Autowired
    private ISchedulerService schedulerService;

    /**
     * 移除任务
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @return 结果
     */
    @RequestMapping(value = "/removeJob")
    public String removeJob
    (
            String jobName,
            String jobGroupName,
            String triggerName,
            String triggerGroupName
    ) throws Exception {
        this.schedulerService.removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
        return "success";
    }

    /**
     * 修改任务时间
     *
     * @param triggerName      触发器名称
     * @param triggerGroupName 触发器组名称
     * @param cron             cron
     * @return 结果
     */
    @RequestMapping(value = "/modifyJobTime")
    public String modifyJobTime
    (
            String triggerName,
            String triggerGroupName,
            String cron
    ) throws Exception {
        this.schedulerService.modifyJobTime(triggerName, triggerGroupName, cron);
        return "success";
    }

    /**
     * 暂停任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名称
     * @return 结果
     */
    @RequestMapping(value = "/pauseJob")
    public String pauseJob(String jobName, String jobGroupName) throws Exception {
        this.schedulerService.pauseJob(jobName, jobGroupName);
        return "success";
    }

    /**
     * 重启任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名称
     * @return 结果
     */
    @RequestMapping(value = "/resumeJob")
    public String resumeJob(String jobName, String jobGroupName) throws Exception {
        this.schedulerService.resumeJob(jobName, jobGroupName);
        return "success";
    }
}
