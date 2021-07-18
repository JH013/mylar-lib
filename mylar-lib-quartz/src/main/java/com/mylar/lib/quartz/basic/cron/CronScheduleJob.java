package com.mylar.lib.quartz.basic.cron;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Cron Schedule Job
 *
 * @author wangz
 * @date 2021/7/18 0018 13:40
 */
public class CronScheduleJob extends QuartzJobBean {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        System.out.println("Quartz cron schedule job executed, name: " + this.name);
    }
}
