package com.mylar.sample.modules.job.distribute;

import com.mylar.lib.base.utils.CryptographyUtils;
import com.mylar.lib.base.utils.ExtUtils;
import com.mylar.lib.guava.core.AbstractLocalCache;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * 简易分布式任务一致性哈希缓存
 *
 * @author wangz
 * @date 2023/3/19 0019 22:58
 */
@Component
public class SimpleDistributeJobConsistentHashLocalCache extends AbstractLocalCache<String, TreeMap<Long, String>> {

    /**
     * 构造方法
     */
    public SimpleDistributeJobConsistentHashLocalCache() {
        this.cacheMaxSize = 10;
        this.expire = ExtUtils.randomInt(6, 9);
        this.timeUnit = TimeUnit.MINUTES;
    }

    // region 常量

    /**
     * 虚拟节点的数目
     */
    private static final int VIRTUAL_NODES = 5000;

    /**
     * 虚拟节点分隔符
     */
    private static final String VIRTUAL_SPLIT = "___";

    /**
     * 内存缓存分隔符
     */
    private static final String CACHE_SPLIT = "@&";

    // endregion

    // region 公共方法

    /**
     * 获取全部节点
     *
     * @param allInstances 全部实例
     * @return 结果
     */
    public TreeMap<Long, String> getAllNodes(List<String> allInstances) {

        // 封装缓存键
        String key = String.join(CACHE_SPLIT, allInstances);

        // 查询缓存
        return this.getCacheThenSource(key);
    }

    // endregion

    // region 重写基类方法

    /**
     * 当缓存不存在时，会调用此函数来加载数据源
     *
     * @param key key
     * @return value\
     */
    @Override
    protected Optional<TreeMap<Long, String>> loadSource(String key) {
        return Optional.of(this.getVirtualNodes(key));
    }

    // endregion

    // region 私有方法

    /**
     * 获取虚拟节点
     *
     * @param cacheKey 缓存键
     * @return 虚拟节点
     */
    private TreeMap<Long, String> getVirtualNodes(String cacheKey) {
        String[] realNodes = cacheKey.split(CACHE_SPLIT);
        return this.initVirtualNodes(Arrays.asList(realNodes));
    }

    /**
     * 初始化虚拟节点
     *
     * @param realNodes 真实节点
     * @return 虚拟节点
     */
    private TreeMap<Long, String> initVirtualNodes(List<String> realNodes) {
        TreeMap<Long, String> virtualNodes = new TreeMap<>();

        // 遍历真实节点
        for (String realNode : realNodes) {

            // 扩展为虚拟节点
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                String virtualNodeName = String.format("%s%s%d", realNode, VIRTUAL_SPLIT, i);
                long hash = CryptographyUtils.getHash(virtualNodeName);
                if (hash != 0L) {
                    virtualNodes.put(hash, realNode);
                }
            }
        }

        return virtualNodes;
    }

    // endregion
}
