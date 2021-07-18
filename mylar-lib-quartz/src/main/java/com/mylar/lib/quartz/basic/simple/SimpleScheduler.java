package com.mylar.lib.quartz.basic.simple;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Simple Scheduler
 *
 * @author wangz
 * @date 2021/7/18 0018 11:40
 */
@Configuration
public class SimpleScheduler {

    @Bean
    public JobDetail simpleJobDetail() {
        return JobBuilder
                .newJob(SimpleScheduleJob.class)
                .withIdentity("simpleJob")
                .usingJobData("name", "simple")
                .storeDurably().build();
    }

    @Bean
    public Trigger simpleJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(2).repeatForever();

        return TriggerBuilder
                .newTrigger()
                .forJob(this.simpleJobDetail())
                .withIdentity("simpleTrigger")
                .withSchedule(scheduleBuilder).build();
    }

}
