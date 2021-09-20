package com.mylar.lib.elasticjob.simple.scriptjob;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
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

import java.util.Objects;

/**
 * 任务配置
 *
 * @author wangz
 * @date 2021/9/20 0020 1:50
 */
@Configuration
public class MyScriptJobConfig {

    @Autowired
    private HikariDataSource hikariDataSource;

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryConfig;

    /**
     * 初始化任务调度
     *
     * @return 任务调度
     */
    @Bean(initMethod = "init")
    public SpringJobScheduler myScriptJobScheduler() {

        // 任务名称
        String jobName = "my_script_job";

        // cron表达式
        String cron = "0/5 * * * * ?";

        // 分片总数
        int shardingTotal = 1;

        // 分片参数
        String shardingItemParameters = "0=text, 1=image, 2=radio, 3=video";

        // 脚本命令
        String scriptCommandLine = Objects.requireNonNull(this.getClass().getClassLoader().getResource("my.bat")).getPath();

        // 任务配置
        LiteJobConfiguration jobConfiguration = this.createJobConfiguration(
                jobName,
                cron,
                shardingTotal,
                shardingItemParameters,
                scriptCommandLine
        );

        // 任务事件配置
        JobEventConfiguration jobEventConfig = new JobEventRdbConfiguration(hikariDataSource);

        // 任务调度
        return new SpringJobScheduler(null, this.coordinatorRegistryConfig, jobConfiguration, jobEventConfig);
    }

    /**
     * 创建任务配置
     *
     * @param jobName                任务名称
     * @param cron                   cron表达式
     * @param shardingTotal          分片总数
     * @param shardingItemParameters 分片系列参数
     * @param scriptCommandLine      脚本路径或命令
     * @return 任务配置
     */
    private LiteJobConfiguration createJobConfiguration
    (
            String jobName,
            String cron,
            int shardingTotal,
            String shardingItemParameters,
            String scriptCommandLine
    ) {

        // 任务核心配置
        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotal);
        if (!StringUtils.isEmpty(shardingItemParameters)) {
            builder.shardingItemParameters(shardingItemParameters);
        }

        JobCoreConfiguration jobCoreConfiguration = builder.build();

        // 任务配置
        ScriptJobConfiguration jobConfiguration = new ScriptJobConfiguration(jobCoreConfiguration, scriptCommandLine);
        return LiteJobConfiguration
                .newBuilder(jobConfiguration)
                .overwrite(true)
                .monitorPort(9886)
                .build();
    }
}

