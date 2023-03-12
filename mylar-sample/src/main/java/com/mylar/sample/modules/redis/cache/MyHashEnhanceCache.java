package com.mylar.sample.modules.redis.cache;

import com.mylar.lib.redis.enhance.AbstractHashEnhanceCache;
import com.mylar.sample.modules.redis.data.MyRedisEntity;

import java.util.List;
import java.util.Map;

/**
 * 自定义缓存 - Hash Enhance
 *
 * @author wangz
 * @date 2023/2/26 0026 15:07
 */
public class MyHashEnhanceCache extends AbstractHashEnhanceCache<MyRedisEntity> {

    /**
     * 构造方法
     *
     * @param cacheKey 缓存键
     */
    public MyHashEnhanceCache(String cacheKey) {
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
     * 获取数据源
     *
     * @param hashField 字段
     * @return 值
     */
    @Override
    protected MyRedisEntity loadSource(String hashField) {
        return new MyRedisEntity(hashField, hashField);
    }

    /**
     * 批量获取数据源
     *
     * @param hashFields 字段集合
     * @return 值集合
     */
    @Override
    protected Map<String, MyRedisEntity> loadSource(List<String> hashFields) {
        return null;
    }

    /**
     * 获取全部数据源
     *
     * @return 值集合
     */
    @Override
    protected Map<String, MyRedisEntity> loadSource() {
        return null;
    }
}
