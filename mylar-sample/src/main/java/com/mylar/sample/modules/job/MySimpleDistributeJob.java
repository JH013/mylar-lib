package com.mylar.sample.modules.job;

import com.mylar.lib.base.utils.JsonUtils;
import com.mylar.lib.job.distribute.annotation.SimpleDistributeJobConfig;
import com.mylar.lib.job.distribute.core.AbstractSimpleDistributeJob;
import com.mylar.lib.job.single.annotation.SingleJobBasicConfig;
import com.mylar.sample.modules.job.distribute.UserJobData;
import com.mylar.sample.modules.job.distribute.strategy.sharding.ConsistentHashJobShardingStrategy;
import com.mylar.sample.modules.job.distribute.strategy.context.SimulateJobContextStrategy;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义简易分布式定时任务
 *
 * @author wangz
 * @date 2023/3/19 0019 22:45
 */
@SingleJobBasicConfig(jobName = "mylar-simple-distribute-job", cron = "0/30 * * * * ?")
@SimpleDistributeJobConfig(
        jobContextStrategy = SimulateJobContextStrategy.class,
        jobShardingStrategy = ConsistentHashJobShardingStrategy.class
)
public class MySimpleDistributeJob extends AbstractSimpleDistributeJob<UserJobData> {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(MySimpleDistributeJob.class);

    // endregion

    // region 重写基类方法

    /**
     * 获取所有任务数据
     *
     * @return 所有任务数据
     */
    @Override
    protected List<UserJobData> getAllJobData() {
        List<UserJobData> allData = new ArrayList<>();
        allData.add(new UserJobData("user_01"));
        allData.add(new UserJobData("user_02"));
        allData.add(new UserJobData("user_03"));
        allData.add(new UserJobData("user_04"));
        allData.add(new UserJobData("user_05"));
        allData.add(new UserJobData("user_06"));
        allData.add(new UserJobData("user_07"));
        allData.add(new UserJobData("user_08"));
        allData.add(new UserJobData("user_09"));
        allData.add(new UserJobData("user_10"));
        allData.add(new UserJobData("user_11"));
        return allData;
    }

    /**
     * 当前分片任务数据处理
     *
     * @param jobExecutionContext 任务执行上下文
     * @param data                当前分片任务数据
     */
    @Override
    protected void runOnCurrentSharding(JobExecutionContext jobExecutionContext, List<UserJobData> data) {
        String jobSign = jobExecutionContext.getJobDetail().getKey().toString();
        this.log.info("[{}]-[{}]-[{}] -> execute {}. ", jobSign, Thread.currentThread().getName(), LocalDateTime.now(), JsonUtils.toJson(data));
    }

    // endregion
}
