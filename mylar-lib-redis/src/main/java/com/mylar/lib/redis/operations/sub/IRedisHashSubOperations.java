package com.mylar.lib.redis.operations.sub;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Redis Sub Operations - Hash
 *
 * @author wangz
 * @date 2023/2/26 0026 4:25
 */
public interface IRedisHashSubOperations {

    /**
     * 获取Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @return Hash值
     */
    String hashGetStr(String cacheKey, String hashKey);

    /**
     * 获取Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKeys Hash键集合
     * @return Hash值集合
     */
    Map<String, String> hashGetStr(String cacheKey, List<String> hashKeys);

    /**
     * 获取Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @param type     值类型
     * @param <T>      泛型
     * @return Hash值
     */
    <T> T hashGet(String cacheKey, String hashKey, Class<T> type);

    /**
     * 获取Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKeys Hash键集合
     * @return Hash值集合
     */
    <T> List<T> hashGet(String cacheKey, List<String> hashKeys, Class<T> type);

    /**
     * 获取全部Hash值
     *
     * @param cacheKey 缓存键
     * @return Hash值集合
     */
    <T> Map<String, T> hashGetAll(String cacheKey, Class<T> type);

    /**
     * 分页扫描获取Hash值
     *
     * @param cacheKey 缓存键
     * @param pageSize 分页大小
     * @param handler  处理方法
     */
    void hashScanAll(String cacheKey, int pageSize, BiConsumer<String, String> handler);

    /**
     * 分页扫描获取Hash值
     *
     * @param cacheKey     缓存键
     * @param matchPattern 匹配模式
     * @param pageSize     分页大小
     * @param handler      处理方法
     */
    void hashScanAll(String cacheKey, String matchPattern, int pageSize, BiConsumer<String, String> handler);

    /**
     * 分页扫描获取Hash值
     *
     * @param cacheKey     缓存键
     * @param matchPattern 匹配模式
     * @param pageSize     分页大小
     * @param type         类型
     * @param handler      处理方法
     * @param <T>          泛型
     */
    <T> void hashScanAll(String cacheKey, String matchPattern, int pageSize, Class<T> type, Consumer<Map<String, T>> handler);

    /**
     * 同步Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @param value    Hash值
     */
    void hashSyncStr(String cacheKey, String hashKey, String value);

    /**
     * 同步Hash值
     *
     * @param cacheKey 缓存键
     * @param values   值集合
     */
    void hashSyncStr(String cacheKey, Map<String, String> values);

    /**
     * 同步Hash值
     *
     * @param cacheKey 缓存键
     * @param values   值集合
     */
    <T> void hashSync(String cacheKey, Map<String, T> values);

    /**
     * 批量删除Hash键
     *
     * @param cacheKey 缓存键
     * @param hashKeys Hash键集合
     */
    void hashRemove(String cacheKey, String... hashKeys);

    /**
     * 获取Hash键数量
     *
     * @param cacheKey 缓存键
     * @return Hash键数量
     */
    Long hashCount(String cacheKey);

    /**
     * 获取全部Hash键
     *
     * @param cacheKey 缓存键
     * @return Hash键集合
     */
    Set<String> hashAllKeys(String cacheKey);

    /**
     * Hash键是否存在
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @return 是否存在
     */
    boolean hashExist(String cacheKey, String hashKey);

    /**
     * 自增
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @param value    计数
     * @return 当前计数
     */
    long hashIncrement(String cacheKey, String hashKey, long value);
}
