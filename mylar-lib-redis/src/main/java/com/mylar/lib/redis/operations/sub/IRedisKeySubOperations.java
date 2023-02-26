package com.mylar.lib.redis.operations.sub;

import java.util.Set;

/**
 * Redis Sub Operations - Key
 *
 * @author wangz
 * @date 2023/2/26 0026 18:25
 */
public interface IRedisKeySubOperations {

    /**
     * 缓存键是否存在
     *
     * @param cacheKey 缓存键
     * @return 是否存在
     */
    boolean hasKey(String cacheKey);

    /**
     * 模糊匹配查询缓存键
     *
     * @param cacheType 缓存类别
     * @param pattern   匹配模式
     * @return 结果
     */
    Set<String> keys(String cacheType, String pattern);

    /**
     * 删除缓存键
     *
     * @param cacheKey 缓存键
     * @return 是否删除成功
     */
    boolean delete(String cacheKey);

    /**
     * 批量删除缓存键
     *
     * @param cacheKeys 缓存键集合
     */
    void delete(String... cacheKeys);

    /**
     * 设置缓存键过期时间（单位：秒）
     *
     * @param cacheKey 缓存键
     * @param timeout  过期时间（单位：秒）
     * @return 是否设置成功
     */
    boolean setExpire(String cacheKey, int timeout);

    /**
     * 获取缓存键过期时间（单位：秒）
     *
     * @param cacheKey 缓存键
     * @return 过期时间（小于 0 说明没有过期时间或缓存键不存在）
     */
    Long getExpire(String cacheKey);
}
