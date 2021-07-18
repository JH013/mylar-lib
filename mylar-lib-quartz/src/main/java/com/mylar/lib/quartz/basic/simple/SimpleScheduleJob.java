package com.mylar.lib.quartz.basic.simple;

import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Simple Schedule Job
 *
 * @author wangz
 * @date 2021/7/18 0018 11:39
 */
public class SimpleScheduleJob extends QuartzJobBean {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        System.out.println("Quartz simple schedule job executed, name: " + this.name);
    }
}
