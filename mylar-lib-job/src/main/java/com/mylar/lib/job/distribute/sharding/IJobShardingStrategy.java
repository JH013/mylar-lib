package com.mylar.lib.job.distribute.sharding;

import com.mylar.lib.job.distribute.core.SimpleDistributeJobData;

import java.util.List;

/**
 * 分片策略
 *
 * @author wangz
 * @date 2023/3/19 0019 21:24
 */
public interface IJobShardingStrategy {

    /**
     * 获取当前分片任务数据集合
     *
     * @param allInstances    所有实例
     * @param currentInstance 当前实例
     * @param allData         所有任务数据
     * @param <T>             任务数据类型
     * @return 当前分片任务数据集合
     */
    <T extends SimpleDistributeJobData> List<T> getShardingData(List<String> allInstances, String currentInstance, List<T> allData);
}
