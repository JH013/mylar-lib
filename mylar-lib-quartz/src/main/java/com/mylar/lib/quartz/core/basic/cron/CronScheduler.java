package com.mylar.lib.quartz.core.basic.cron;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cron Scheduler
 *
 * @author wangz
 * @date 2021/7/18 0018 22:05
 */
@Configuration
public class CronScheduler {

    @Bean
    public JobDetail cronJobDetail() {
        return JobBuilder
                .newJob(CronScheduleJob.class)
                .withIdentity("cronJob")
                .usingJobData("name", "cron")
                .storeDurably().build();
    }

    @Bean
    public Trigger cronJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/3 * * * * ?");
        return TriggerBuilder
                .newTrigger()
                .forJob(this.cronJobDetail())
                .withIdentity("cronTrigger")
                .withSchedule(scheduleBuilder).build();
    }

}
