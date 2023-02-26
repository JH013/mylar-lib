package com.mylar.lib.redis.enhance;

import java.util.Map;

/**
 * HASH缓存接口
 *
 * @author wangz
 * @date 2023/2/26 0026 21:13
 */
public interface IHashCache<T> {

    /**
     * 获取值类型
     *
     * @return 值类型
     */
    Class<T> getValueClazz();

    // region 查询缓存

    /**
     * 查询缓存
     *
     * @param cacheKey  缓存键
     * @param hashField 字段
     * @return 值
     */
    T getCache(String cacheKey, String hashField);

    /**
     * 查询缓存
     *
     * @param cacheKey   缓存键
     * @param hashFields 字段集合
     * @return 值集合
     */
    Map<String, T> getCache(String cacheKey, String... hashFields);

    /**
     * 查询缓存
     *
     * @param cacheKey 缓存键
     * @return 值集合
     */
    Map<String, T> getCache(String cacheKey);

    /**
     * 字段是否存在
     *
     * @param cacheKey  缓存键
     * @param hashField 字段
     * @return 是否存在
     */
    Boolean hashExist(String cacheKey, String hashField);

    // endregion

    // region 同步缓存

    /**
     * 同步缓存
     *
     * @param cacheKey  缓存键
     * @param hashField 字段
     * @param hashValue 值
     */
    void syncCache(String cacheKey, String hashField, T hashValue);

    /**
     * 同步缓存
     *
     * @param cacheKey   缓存键
     * @param hashValues 值集合
     */
    void syncCache(String cacheKey, Map<String, T> hashValues);

    // endregion

    // region 删除缓存

    /**
     * 删除缓存
     *
     * @param cacheKey   缓存键
     * @param hashFields 字段集合
     */
    void removeCache(String cacheKey, String... hashFields);

    // endregion
}
