package com.mylar.lib.elasticjob.simple.simplejob;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 任务配置
 *
 * @author wangz
 * @date 2021/9/19 0019 20:29
 */
@Configuration
public class MySimpleJobConfig {

    @Autowired
    private HikariDataSource hikariDataSource;

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryConfig;

    @Autowired
    private MySimpleJob mySimpleJob;

    /**
     * 初始化任务调度
     *
     * @return 任务调度
     */
    @Bean(initMethod = "init")
    public SpringJobScheduler mySimpleJobScheduler() {

        //创建SpringJobScheduler

        // cron表达式
        String cron = "0/10 * * * * ?";

        // 分片总数
        int shardingTotal = 3;

        // 分片参数
        String shardingItemParameters = "0=text, 1=image, 2=radio, 3=video";

        // 任务配置
        LiteJobConfiguration jobConfiguration = this.createJobConfiguration(
                this.mySimpleJob.getClass(),
                cron,
                shardingTotal,
                shardingItemParameters
        );

        // 任务事件配置
        JobEventConfiguration jobEventConfig = new JobEventRdbConfiguration(hikariDataSource);

        // 任务调度
        return new SpringJobScheduler(this.mySimpleJob, this.coordinatorRegistryConfig, jobConfiguration, jobEventConfig);
    }

    /**
     * 创建任务配置
     *
     * @param jobClass               任务类
     * @param cron                   cron表达式
     * @param shardingTotal          分片总数
     * @param shardingItemParameters 分片系列参数
     * @return 任务配置
     */
    private LiteJobConfiguration createJobConfiguration
    (
            Class<? extends SimpleJob> jobClass,
            String cron,
            int shardingTotal,
            String shardingItemParameters
    ) {

        // 任务核心配置
        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotal);
        if (!StringUtils.isEmpty(shardingItemParameters)) {
            builder.shardingItemParameters(shardingItemParameters);
        }

        JobCoreConfiguration jobCoreConfiguration = builder.build();

        // 任务配置
        SimpleJobConfiguration jobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, jobClass.getCanonicalName());
        return LiteJobConfiguration
                .newBuilder(jobConfiguration)
                .overwrite(true)
                .monitorPort(9889)
                .build();
    }
}
