package com.mylar.lib.redis.enhance;

import java.util.Map;

/**
 * HASH缓存增强
 *
 * @author wangz
 * @date 2023/2/26 0026 22:19
 */
public interface IHashEnhanceCache<T> {

    /**
     * 查询缓存
     * <p>
     * 1、单个查询
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @param hashField 字段
     * @return 值
     */
    T getAndSyncIfAbsent(String hashField);

    /**
     * 查询缓存
     * <p>
     * 1、批量查询
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @param hashFields 字段集合
     * @return 值集合
     */
    Map<String, T> getAndSyncIfAbsent(String... hashFields);

    /**
     * 查询缓存
     * <p>
     * 1、查询全部
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @return 值集合
     */
    Map<String, T> getAndSyncIfAbsent();
}
