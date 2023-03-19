package com.mylar.lib.job.distribute.core;

import com.mylar.lib.base.enhance.SpringResolver;
import com.mylar.lib.job.distribute.context.SimpleDistributeJobContext;
import com.mylar.lib.job.single.core.AbstractSingleJob;
import com.mylar.lib.job.distribute.annotation.SimpleDistributeJobConfig;
import com.mylar.lib.job.distribute.context.IJobContextStrategy;
import com.mylar.lib.job.distribute.sharding.IJobShardingStrategy;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 简易分布式定时任务抽象基类
 *
 * @author wangz
 * @date 2023/3/19 0019 21:16
 */
public abstract class AbstractSimpleDistributeJob<T extends SimpleDistributeJobData> extends AbstractSingleJob {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(AbstractSimpleDistributeJob.class);

    // endregion

    // region 重写基类方法

    /**
     * 任务执行
     *
     * @param jobExecutionContext 任务执行上下文
     */
    @Override
    protected void doWork(JobExecutionContext jobExecutionContext) {

        // 获取注解
        SimpleDistributeJobConfig[] jobConfigs = getClass().getAnnotationsByType(SimpleDistributeJobConfig.class);
        if (jobConfigs.length == 0) {
            log.warn("Simple distribute job run failed, lack necessary config.");
            return;
        }

        // 获取上下文策略
        IJobContextStrategy contextStrategy = SpringResolver.resolve(jobConfigs[0].jobContextStrategy());

        // 获取任务上下文
        SimpleDistributeJobContext jobContext = contextStrategy.getJobContext();
        if (jobContext == null) {
            log.warn("Simple distribute job run failed, job context is empty.");
            return;
        }

        // 所有任务数据
        List<T> allJobData = this.getAllJobData();
        if (CollectionUtils.isEmpty(allJobData)) {
            log.warn("Simple distribute job run failed, all job data is empty.");
            return;
        }

        // 获取分片策略
        IJobShardingStrategy jobShardingStrategy = SpringResolver.resolve(jobConfigs[0].jobShardingStrategy());

        // 获取当前分片任务数据
        List<T> shardingData = jobShardingStrategy.getShardingData(jobContext.getAllInstance(), jobContext.getCurrentInstance(), allJobData);

        // 当前分片任务数据处理
        this.runOnCurrentSharding(shardingData);
    }

    // endregion

    // region 供子类重写

    /**
     * 获取所有任务数据
     *
     * @return 所有任务数据
     */
    protected abstract List<T> getAllJobData();

    /**
     * 当前分片任务数据处理
     *
     * @param data 当前分片任务数据
     */
    protected abstract void runOnCurrentSharding(List<T> data);

    // endregion
}
