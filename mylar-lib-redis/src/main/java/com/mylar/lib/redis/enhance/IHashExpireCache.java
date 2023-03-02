package com.mylar.lib.redis.enhance;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 支持 HashKey 过期的 HASH 缓存
 *
 * @author wangz
 * @date 2023/2/28 0028 23:13
 */
public interface IHashExpireCache<T> {

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
     * 分页获取某个Hash下所有缓存，并且处理
     *
     * @param pageSize 分页大小，每页scan的数据量
     * @param handler  处理方法
     */
    void scanAll(int pageSize, Consumer<T> handler);

    /**
     * 更新缓存（支持字段过期）
     *
     * @param hashField 字段
     * @param hashValue 值
     * @return 是否成功
     */
    boolean hashExpireSet(String hashField, T hashValue);

    /**
     * 批量更新缓存（支持字段过期）
     *
     * @param hashValues 值集合
     * @return 成功数量
     */
    int hashExpireMultiSet(Map<String, T> hashValues);

    /**
     * 批量更新缓存-当HashKey不存在时更新（支持字段过期）
     *
     * @param hashValues 值集合
     * @return 成功数量
     */
    int hashExpireMultiSetNx(Map<String, T> hashValues);

    /**
     * 删除缓存
     *
     * @param hashFields 字段集合
     * @return 删除个数
     */
    int remove(String... hashFields);
}
