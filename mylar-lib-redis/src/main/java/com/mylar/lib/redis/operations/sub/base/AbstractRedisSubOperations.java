package com.mylar.lib.redis.operations.sub.base;

import com.mylar.lib.redis.core.RedisTemplateCache;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis Sub Operations
 *
 * @author wangz
 * @date 2023/2/26 0026 3:56
 */
public abstract class AbstractRedisSubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public AbstractRedisSubOperations(RedisTemplateCache redisTemplateCache) {
        this.redisTemplateCache = redisTemplateCache;
    }

    // region 变量

    /**
     * RedisTemplate 缓存
     */
    private final RedisTemplateCache redisTemplateCache;

    // endregion

    // region 保护方法

    /**
     * 获取 RedisTemplate
     *
     * @return RedisTemplate
     */
    protected StringRedisTemplate getTemplate(String cacheKey) {
        return this.redisTemplateCache.getRedisTemplate(cacheKey);
    }

    // endregion
}
