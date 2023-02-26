package com.mylar.sample.redis.cache;

import com.mylar.lib.redis.enhance.AbstractListEnhanceCache;
import com.mylar.sample.redis.data.MyRedisEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义缓存 - Hash Enhance
 *
 * @author wangz
 * @date 2023/2/26 0026 23:37
 */
public class MyListEnhanceCache extends AbstractListEnhanceCache<MyRedisEntity> {

    /**
     * 构造方法
     *
     * @param cacheKey 缓存键
     */
    public MyListEnhanceCache(String cacheKey) {
        super(cacheKey);
        this.minCacheKeyTimeOut = 30;
        this.maxCacheKeyTimeOut = 30;
    }

    /**
     * 获取值类型
     *
     * @return 值类型
     */
    @Override
    public Class<MyRedisEntity> getValueClazz() {
        return MyRedisEntity.class;
    }

    /**
     * 获取全部数据源
     *
     * @return 结果
     */
    @Override
    protected List<MyRedisEntity> loadSource() {
        List<MyRedisEntity> entities = new ArrayList<>();
        entities.add(new MyRedisEntity("xiao_a", "18"));
        entities.add(new MyRedisEntity("xiao_b", "19"));
        entities.add(new MyRedisEntity("xiao_c", "16"));
        return entities;
    }
}
