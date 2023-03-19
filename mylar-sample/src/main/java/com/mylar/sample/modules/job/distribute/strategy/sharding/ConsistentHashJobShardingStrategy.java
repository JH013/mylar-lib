package com.mylar.sample.modules.job.distribute.strategy.sharding;

import com.mylar.lib.base.utils.CryptographyUtils;
import com.mylar.lib.job.distribute.core.SimpleDistributeJobData;
import com.mylar.lib.job.distribute.sharding.IJobShardingStrategy;
import com.mylar.sample.modules.job.distribute.SimpleDistributeJobConsistentHashLocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 任务分片策略
 * <p>
 * 一致性hash的分片策略, 每个实际的服务器节点会扩充到n倍的虚拟几点，来保证均衡性
 *
 * @author wangz
 * @date 2023/3/19 0019 23:40
 */
@Component
public class ConsistentHashJobShardingStrategy implements IJobShardingStrategy {

    // region 变量

    /**
     * 内存缓存
     */
    @Autowired
    private SimpleDistributeJobConsistentHashLocalCache localCache;

    // endregion

    // region 接口实现

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

        // 初始化虚拟节点
        TreeMap<Long, String> virtualNodes = this.localCache.getAllNodes(allInstances);
        for (T dataItem : allData) {

            // 任务数据唯一标识进行hash
            long hash = CryptographyUtils.getHash(dataItem.uniqueSign());

            // 获取大于该hash值的第一个节点
            String matchedNode;
            Map.Entry<Long, String> ceilingEntry = virtualNodes.ceilingEntry(hash);
            if (ceilingEntry == null) {
                matchedNode = virtualNodes.get(virtualNodes.firstKey());
            } else {
                matchedNode = ceilingEntry.getValue();
            }

            // 判断匹配到的实例是否为当前实例
            if (currentInstance.equals(matchedNode)) {
                taskData.add(dataItem);
            }
        }

        return taskData;
    }

    // endregion
}
