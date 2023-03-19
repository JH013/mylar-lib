package com.mylar.lib.job.distribute.context;

/**
 * 上下文策略
 *
 * @author wangz
 * @date 2023/3/19 0019 21:26
 */
public interface IJobContextStrategy {

    /**
     * 获取任务上下文
     *
     * @return 任务上下文
     */
    SimpleDistributeJobContext getJobContext();
}
