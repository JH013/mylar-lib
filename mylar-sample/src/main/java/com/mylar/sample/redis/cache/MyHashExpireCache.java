package com.mylar.sample.redis.cache;

import com.mylar.lib.redis.enhance.AbstractHashExpireCache;
import com.mylar.sample.redis.data.MyRedisEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义缓存 - Hash Expire
 *
 * @author wangz
 * @date 2023/2/28 0028 23:21
 */
public class MyHashExpireCache extends AbstractHashExpireCache<MyRedisEntity> {

    /**
     * 构造方法
     *
     * @param cacheKey 缓存键
     */
    public MyHashExpireCache(String cacheKey) {
        super(cacheKey);
        this.minFieldTimeout = 120;
        this.maxFieldTimeout = 120;
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
        Map<String, MyRedisEntity> map = new HashMap<>();
        for (String hashField : hashFields) {
            map.put(hashField, new MyRedisEntity(hashField, hashField));
        }

        return map;
    }
}
