package com.mylar.lib.job.distribute.sharding.plugins;

import com.mylar.lib.job.distribute.core.SimpleDistributeJobData;
import com.mylar.lib.job.distribute.sharding.IJobShardingStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务分片策略
 * <p>
 * 平均分配
 *
 * @author wangz
 * @date 2023/3/19 0019 21:25
 */
@Component
public class AverageJobShardingStrategy implements IJobShardingStrategy {

    /**
     * 获取当前分片任务数据集合
     *
     * @param allInstances    所有实例
     * @param currentInstance 当前实例
     * @param allData         所有任务数据
     * @param <T>             任务数据类型
     * @return 当前分片任务数据集合
     */
    @Override
    public <T extends SimpleDistributeJobData> List<T> getShardingData(List<String> allInstances, String currentInstance, List<T> allData) {

        // 当前分片任务数据集合
        List<T> taskData = new ArrayList<>();

        // 分片总数
        int shardingTotal = allInstances.size();

        // 当前分片索引
        int shardingIndex = allInstances.indexOf(currentInstance);

        while (shardingIndex < allData.size()) {
            taskData.add(allData.get(shardingIndex));
            shardingIndex += shardingTotal;
        }

        return taskData;
    }
}
