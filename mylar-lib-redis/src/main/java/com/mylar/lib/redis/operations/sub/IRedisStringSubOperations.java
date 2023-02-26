package com.mylar.lib.redis.operations.sub;

import java.util.List;

/**
 * Redis Sub Operations - String
 *
 * @author wangz
 * @date 2023/2/26 0026 3:50
 */
public interface IRedisStringSubOperations {

    /**
     * 获取缓存值
     *
     * @param cacheKey 缓存键
     * @return 缓存值
     */
    String get(String cacheKey);

    /**
     * 批量获取缓存值（key 不存在时会返回 null）
     *
     * @param cacheKeys 缓存键集合
     * @return 缓存值集合
     */
    List<String> get(String... cacheKeys);

    /**
     * 设置缓存值
     *
     * @param cacheKey 缓存键
     * @param value    缓存值
     */
    void set(String cacheKey, String value);

    /**
     * 设置缓存值（附带过期时间）
     *
     * @param cacheKey 缓存键
     * @param value    缓存值
     * @param timeout  过期时间（单位：秒）
     */
    void set(String cacheKey, String value, long timeout);
}
