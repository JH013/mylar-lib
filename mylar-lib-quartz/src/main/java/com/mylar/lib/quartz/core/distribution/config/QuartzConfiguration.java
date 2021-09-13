package com.mylar.lib.quartz.core.distribution.config;

import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

/**
 * Quartz配置
 *
 * @author wangz
 * @date 2021/9/9 0009 1:21
 */
@Configuration
@EnableScheduling
public class QuartzConfiguration {

    /**
     * 配置任务工厂实例
     *
     * @param applicationContext spring上下文实例
     * @return 任务工厂
     */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        MySpringBeanJobFactory jobFactory = new MySpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * 配置任务调度器，使用项目数据源作为quartz数据源
     *
     * @param jobFactory 自定义配置任务工厂
     * @param dataSource 数据源实例
     * @return 调度器
     */
    @Bean(destroyMethod = "destroy", autowire = Autowire.NO)
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        // 将spring管理job自定义工厂交由调度器维护
        schedulerFactoryBean.setJobFactory(jobFactory);

        // 设置覆盖已存在的任务
        schedulerFactoryBean.setOverwriteExistingJobs(true);

        // 项目启动完成后，等待2秒后开始执行调度器初始化
        schedulerFactoryBean.setStartupDelay(2);

        // 设置调度器自动运行
        schedulerFactoryBean.setAutoStartup(true);

        // 设置数据源，使用与项目统一数据源
        schedulerFactoryBean.setDataSource(dataSource);

        // 设置上下文spring bean name
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");

        // 设置配置文件位置
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("/quartz.properties"));
        return schedulerFactoryBean;
    }
}
