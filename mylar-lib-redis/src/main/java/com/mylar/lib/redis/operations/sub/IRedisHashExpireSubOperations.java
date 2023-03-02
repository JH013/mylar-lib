package com.mylar.lib.redis.operations.sub;

import com.mylar.lib.redis.data.HashExpireOriginalVersionValue;
import com.mylar.lib.redis.data.HashExpireValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Redis Sub Operations - Hash Expire
 *
 * @author wangz
 * @date 2023/2/27 0027 1:14
 */
public interface IRedisHashExpireSubOperations {

    /**
     * 单个查询
     *
     * @param cacheKey  缓存键
     * @param hashField Hash键
     * @return 结果
     */
    String hashExpireGet(String cacheKey, String hashField);

    /**
     * 批量查询
     *
     * @param cacheKey   缓存键
     * @param hashFields Hash键
     * @return 结果
     */
    Map<String, String> hashExpireBatchGet(String cacheKey, String... hashFields);

    /**
     * 扫描缓存
     *
     * @param cacheKey     缓存键
     * @param pageSize     分页大小
     * @param valueHandler 值处理方法
     */
    void hashExpireScan(String cacheKey, int pageSize, Consumer<String> valueHandler);

    /**
     * 扫描缓存
     *
     * @param cacheKey     缓存键
     * @param matchPattern 匹配模式
     * @param pageSize     分页大小
     * @param valueHandler 值处理方法
     */
    void hashExpireScan(String cacheKey, String matchPattern, int pageSize, Consumer<String> valueHandler);

    /**
     * 扫描并处理已过期的 Hash 键
     *
     * @param cacheKey       缓存键
     * @param matchPattern   匹配模式
     * @param pageSize       分页大小
     * @param valueHandler   值处理方法
     * @param expiredHandler 过期 Hash 键处理方法（为空时默认清除已过期的 Hash 键）
     */
    void hashExpireScanAndDealExpiredField(
            String cacheKey,
            String matchPattern,
            int pageSize,
            Consumer<String> valueHandler,
            Consumer<List<String>> expiredHandler
    );

    /**
     * 单个更新
     *
     * @param cacheKey  缓存键
     * @param hashField Hash 键
     * @param hashValue Hash 值
     * @param timeout   过期时间
     * @param unit      单位
     * @return 结果
     */
    boolean hashExpireSet(String cacheKey, String hashField, String hashValue, long timeout, TimeUnit unit);

    /**
     * 批量更新
     *
     * @param cacheKey        缓存键
     * @param hashFieldValues Hash 键值对
     * @param timeout         过期时间
     * @param unit            单位
     * @return 结果
     */
    int hashExpireBatchSet(String cacheKey, Map<String, String> hashFieldValues, long timeout, TimeUnit unit);

    /**
     * 批量更新-当HashKey不存在时更新
     *
     * @param cacheKey        缓存键
     * @param hashFieldValues Hash 键值对
     * @param timeout         过期时间
     * @param unit            单位
     * @return 结果
     */
    int hashExpireBatchSetNx(String cacheKey, Map<String, String> hashFieldValues, long timeout, TimeUnit unit);

    /**
     * 批量更新-根据版本号更新
     *
     * @param cacheKey        缓存键
     * @param hashFieldValues Hash 键值对
     * @return 结果
     */
    int hashExpireBatchSetVer(String cacheKey, Map<String, HashExpireOriginalVersionValue> hashFieldValues);

    /**
     * 批量删除 HashKey
     *
     * @param cacheKey   缓存键
     * @param hashFields Hash 键集合
     * @return 结果
     */
    int hashExpireRemoveHashKey(String cacheKey, String... hashFields);

    /**
     * 删除缓存键
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    int hashExpireRemoveCacheKey(String cacheKey);
}
