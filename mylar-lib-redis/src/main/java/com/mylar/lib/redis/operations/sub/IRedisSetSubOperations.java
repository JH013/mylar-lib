package com.mylar.lib.redis.operations.sub;

import java.util.List;

/**
 * Redis Sub Operations - Set
 *
 * @author wangz
 * @date 2023/2/26 0026 4:26
 */
public interface IRedisSetSubOperations {

    /**
     * 弹出一个元素
     *
     * @param cacheKey 缓存键
     * @param type     类型
     * @param <T>      泛型
     * @return 结果
     */
    <T> T setPop(String cacheKey, Class<T> type);

    /**
     * 弹出指定数量元素
     *
     * @param cacheKey 缓存键
     * @param type     类型
     * @param count    数量
     * @param <T>      泛型
     * @return 结果
     */
    <T> List<T> setPopByCount(String cacheKey, Class<T> type, long count);

    /**
     * 同步缓存
     *
     * @param cacheKey 缓存键
     * @param values   缓存值
     * @param <T>      泛型
     */
    <T> void setSync(String cacheKey, List<T> values);
}
