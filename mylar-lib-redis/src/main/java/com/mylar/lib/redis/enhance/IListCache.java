package com.mylar.lib.redis.enhance;

import java.util.List;

/**
 * LIST缓存接口
 *
 * @author wangz
 * @date 2023/2/26 0026 22:27
 */
public interface IListCache<T> {

    /**
     * 获取值类型
     *
     * @return 值类型
     */
    Class<T> getValueClazz();

    /**
     * 查询缓存
     *
     * @param cacheKey 缓存键
     * @param index    索引
     * @return 结果
     */
    T getCache(String cacheKey, long index);

    /**
     * 查询缓存（查询全部）
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    List<T> getCache(String cacheKey);

    /**
     * 增量同步缓存
     *
     * @param cacheKey 缓存键
     * @param value    值
     */
    void syncCacheIncrement(String cacheKey, T value);

    /**
     * 增量同步缓存
     *
     * @param cacheKey 缓存键
     * @param values   值
     */
    void syncCacheIncrement(String cacheKey, List<T> values);

    /**
     * 全量同步缓存
     *
     * @param cacheKey 缓存键
     * @param values   值
     */
    void syncCacheFull(String cacheKey, List<T> values);
}