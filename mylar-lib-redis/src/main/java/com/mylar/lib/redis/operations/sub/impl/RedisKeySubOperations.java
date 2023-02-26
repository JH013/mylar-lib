package com.mylar.lib.redis.operations.sub.impl;

import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.operations.sub.IRedisKeySubOperations;
import com.mylar.lib.redis.operations.sub.base.AbstractRedisSubOperations;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis Sub Operations - Key
 *
 * @author wangz
 * @date 2023/2/26 0026 18:25
 */
public class RedisKeySubOperations extends AbstractRedisSubOperations implements IRedisKeySubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public RedisKeySubOperations(RedisTemplateCache redisTemplateCache) {
        super(redisTemplateCache);
    }

    // region 接口实现

    /**
     * 缓存键是否存在
     *
     * @param cacheKey 缓存键
     * @return 是否存在
     */
    @Override
    public boolean hasKey(String cacheKey) {
        return Boolean.TRUE.equals(this.getTemplate(cacheKey).hasKey(cacheKey));
    }

    /**
     * 模糊匹配查询缓存键
     *
     * @param cacheType 缓存类别
     * @param pattern   匹配模式
     * @return 结果
     */
    @Override
    public Set<String> keys(String cacheType, String pattern) {
        return this.getTemplate(cacheType).keys(pattern);
    }

    /**
     * 删除缓存键
     *
     * @param cacheKey 缓存键
     * @return 是否删除成功
     */
    @Override
    public boolean delete(String cacheKey) {
        return Boolean.TRUE.equals(this.getTemplate(cacheKey).delete(cacheKey));
    }

    /**
     * 批量删除缓存键
     *
     * @param cacheKeys 缓存键集合
     */
    @Override
    public void delete(String... cacheKeys) {
        if (ArrayUtils.isEmpty(cacheKeys)) {
            return;
        }

        this.getTemplate(cacheKeys[0]).delete(Arrays.asList(cacheKeys));
    }

    /**
     * 设置缓存键过期时间（单位：秒）
     *
     * @param cacheKey 缓存键
     * @param timeout  过期时间（单位：秒）
     * @return 是否设置成功
     */
    @Override
    public boolean setExpire(String cacheKey, int timeout) {
        return Boolean.TRUE.equals(this.getTemplate(cacheKey).expire(cacheKey, timeout, TimeUnit.SECONDS));
    }

    /**
     * 获取缓存键过期时间（单位：秒）
     *
     * @param cacheKey 缓存键
     * @return 过期时间（小于 0 说明没有过期时间或缓存键不存在）
     */
    @Override
    public Long getExpire(String cacheKey) {
        return this.getTemplate(cacheKey).getExpire(cacheKey, TimeUnit.SECONDS);
    }

    // endregion
}
