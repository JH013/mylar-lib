package com.mylar.lib.redis.operations.sub.impl;

import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.operations.sub.base.AbstractRedisSubOperations;
import com.mylar.lib.redis.operations.sub.IRedisStringSubOperations;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis Sub Operations - String
 *
 * @author wangz
 * @date 2023/2/26 0026 3:57
 */
public class RedisStringSubOperations extends AbstractRedisSubOperations implements IRedisStringSubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public RedisStringSubOperations(RedisTemplateCache redisTemplateCache) {
        super(redisTemplateCache);
    }

    // region 接口实现

    /**
     * 获取缓存值
     *
     * @param cacheKey 缓存键
     * @return 缓存值
     */
    @Override
    public String get(String cacheKey) {
        return this.getTemplate(cacheKey).opsForValue().get(cacheKey);
    }

    /**
     * 批量获取缓存值（key 不存在时会返回 null）
     *
     * @param cacheKeys 缓存键集合
     * @return 缓存值集合
     */
    @Override
    public List<String> get(String... cacheKeys) {
        if (ArrayUtils.isEmpty(cacheKeys)) {
            return Collections.emptyList();
        }

        return this.getTemplate(cacheKeys[0]).opsForValue().multiGet(Arrays.asList(cacheKeys));
    }

    /**
     * 设置缓存值
     *
     * @param cacheKey 缓存键
     * @param value    缓存值
     */
    @Override
    public void set(String cacheKey, String value) {
        this.getTemplate(cacheKey).opsForValue().set(cacheKey, value);
    }

    /**
     * 设置缓存值（附带过期时间）
     *
     * @param cacheKey 缓存键
     * @param value    缓存值
     * @param timeout  过期时间（单位：秒）
     */
    @Override
    public void set(String cacheKey, String value, long timeout) {
        this.getTemplate(cacheKey).opsForValue().set(cacheKey, value, timeout, TimeUnit.SECONDS);
    }

    // endregion
}
