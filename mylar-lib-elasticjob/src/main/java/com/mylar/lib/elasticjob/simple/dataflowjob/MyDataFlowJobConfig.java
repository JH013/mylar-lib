package com.mylar.lib.elasticjob.simple.dataflowjob;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
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
 * @date 2021/9/20 0020 1:23
 */
@Configuration
public class MyDataFlowJobConfig {

    @Autowired
    private HikariDataSource hikariDataSource;

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryConfig;

    @Autowired
    private MyDataFlowJob myDataFlowJob;

    /**
     * 初始化任务调度
     *
     * @return 任务调度
     */
    @Bean(initMethod = "init")
    public SpringJobScheduler myDataFlowJobScheduler() {

        //创建SpringJobScheduler

        // cron表达式
        String cron = "0/20 * * * * ?";

        // 分片总数
        int shardingTotal = 3;

        // 分片参数
        String shardingItemParameters = "0=text, 1=image, 2=radio, 3=video";

        // 是否流式作业
        boolean streamingProcess = false;

        // 任务配置
        LiteJobConfiguration jobConfiguration = this.createJobConfiguration(
                this.myDataFlowJob.getClass(),
                cron,
                shardingTotal,
                shardingItemParameters,
                streamingProcess
        );

        // 任务事件配置
        JobEventConfiguration jobEventConfig = new JobEventRdbConfiguration(hikariDataSource);

        // 任务调度
        return new SpringJobScheduler(this.myDataFlowJob, this.coordinatorRegistryConfig, jobConfiguration, jobEventConfig);
    }

    /**
     * 创建任务配置
     *
     * @param jobClass               任务类
     * @param cron                   cron表达式
     * @param shardingTotal          分片总数
     * @param shardingItemParameters 分片系列参数
     * @param streamingProcess       是否流式作业（true: 除非fetchData返回数据为null或size=0，否则会一直执行）
     * @return 任务配置
     */
    private LiteJobConfiguration createJobConfiguration
    (
            Class<? extends DataflowJob<?>> jobClass,
            String cron,
            int shardingTotal,
            String shardingItemParameters,
            boolean streamingProcess
    ) {

        // 任务核心配置
        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotal);
        if (!StringUtils.isEmpty(shardingItemParameters)) {
            builder.shardingItemParameters(shardingItemParameters);
        }

        JobCoreConfiguration jobCoreConfiguration = builder.build();

        // 任务配置
        DataflowJobConfiguration jobConfiguration = new DataflowJobConfiguration(jobCoreConfiguration, jobClass.getCanonicalName(), streamingProcess);
        return LiteJobConfiguration
                .newBuilder(jobConfiguration)
                .overwrite(true)
                .monitorPort(9888)
                .build();
    }
}
