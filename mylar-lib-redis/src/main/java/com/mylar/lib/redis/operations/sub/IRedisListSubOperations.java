package com.mylar.lib.redis.operations.sub;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis Sub Operations - List
 *
 * @author wangz
 * @date 2023/2/26 0026 4:25
 */
public interface IRedisListSubOperations {

    /**
     * 按范围查询
     *
     * @param cacheKey   缓存键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 结果
     */
    List<String> listGetByRange(String cacheKey, long startIndex, long endIndex);

    /**
     * 按范围查询
     *
     * @param cacheKey   缓存键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @param type       类型
     * @param <T>        泛型
     * @return 结果
     */
    <T> List<T> listGetByRange(String cacheKey, long startIndex, long endIndex, Class<T> type);

    /**
     * 按索引查询
     *
     * @param cacheKey 缓存键
     * @param index    索引
     * @param type     类型
     * @param <T>      泛型
     * @return 结果
     */
    <T> T listGetByIndex(String cacheKey, long index, Class<T> type);

    /**
     * 查询长度
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    Long listGetSize(String cacheKey);

    /**
     * 增量同步缓存
     *
     * @param cacheKey 缓存键
     * @param values   缓存值
     * @param leftPush 队首插入
     * @param <T>      泛型
     */
    <T> void listSyncByIncrement(String cacheKey, List<T> values, boolean leftPush);

    /**
     * 按索引同步
     *
     * @param cacheKey 缓存键
     * @param index    索引
     * @param value    值
     * @param <T>      泛型
     */
    <T> void listSyncByIndex(String cacheKey, long index, T value);

    /**
     * 弹出缓存元素
     *
     * @param cacheKey 缓存键
     * @param timeout  没有值时阻塞超时时间（0 不阻塞）
     * @param unit     没有值时阻塞超时时间单位（null 不阻塞）
     * @param leftPop  队首弹出
     * @param type     类型
     * @param <T>      泛型
     * @return 结果
     */
    <T> T listPop(String cacheKey, long timeout, TimeUnit unit, boolean leftPop, Class<T> type);
}
