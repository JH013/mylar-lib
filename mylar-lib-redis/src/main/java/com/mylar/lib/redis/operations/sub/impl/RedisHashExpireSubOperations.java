package com.mylar.lib.redis.operations.sub.impl;

import com.google.common.collect.Lists;
import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.data.HashExpireValue;
import com.mylar.lib.redis.operations.sub.IRedisHashExpireSubOperations;
import com.mylar.lib.redis.operations.sub.IRedisScriptSubOperations;
import com.mylar.lib.redis.operations.sub.base.AbstractRedisSubOperations;
import com.mylar.lib.redis.script.HashExpireRedisScript;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Redis Sub Operations - Hash Expire
 *
 * @author wangz
 * @date 2023/2/27 0027 1:15
 */
public class RedisHashExpireSubOperations extends AbstractRedisSubOperations implements IRedisHashExpireSubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public RedisHashExpireSubOperations(RedisTemplateCache redisTemplateCache) {
        super(redisTemplateCache);

        // 初始化 Redis Operations
        this.scriptOperations = new RedisScriptSubOperations(redisTemplateCache);
    }

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(RedisHashExpireSubOperations.class);

    /**
     * Redis Sub Operations - Lua Script
     */
    private final IRedisScriptSubOperations scriptOperations;

    /**
     * 默认批量更新 HashKey 分页大小：100
     */
    private static final int DEFAULT_BATCH_SET_PAGE_SIZE = 100;

    /**
     * 默认缓存键过期时间：1 天
     */
    private static final String DEFAULT_CACHE_KEY_EXPIRE = "86400";

    // endregion

    // region 接口实现

    /**
     * 单个查询
     *
     * @param cacheKey  缓存键
     * @param hashField Hash键
     * @return 结果
     */
    @Override
    public String hashExpireGet(String cacheKey, String hashField) {

        // 执行批量查询
        Map<String, String> map = this.hashExpireBatchGet(cacheKey, hashField);
        if (map.isEmpty()) {
            return null;
        }

        return map.get(hashField);
    }

    /**
     * 批量查询
     *
     * @param cacheKey   缓存键
     * @param hashFields Hash键
     * @return 结果
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> hashExpireBatchGet(String cacheKey, String... hashFields) {

        // 查询结果
        Map<String, String> result = new HashMap<>();
        try {

            // 键集合
            List<String> keys = Collections.singletonList(cacheKey);

            // 参数集合
            List<String> args = new ArrayList<>();

            // 参数 1：缓存键过期时间
            args.add(DEFAULT_CACHE_KEY_EXPIRE);

            // 参数 2：当前时间
            args.add(String.valueOf(Instant.now().getEpochSecond()));

            // 其他参数：HashKey 集合
            args.addAll(Arrays.asList(hashFields));

            // 执行脚本：批量查询
            Object luaResult = this.scriptOperations.executeScript(HashExpireRedisScript.singleton().luaBatchGet(), keys, args);

            // 执行结果为空
            if (luaResult == null) {
                return new HashMap<>();
            }

            // 遍历执行结果，封装返回值
            List<Object> listResult = (List<Object>) luaResult;
            int size = hashFields.length;
            if (listResult.size() == size) {
                for (int i = 0; i < size; i++) {
                    Object hashValue = listResult.get(i);
                    result.put(hashFields[i], hashValue == null ? null : hashValue.toString());
                }
            }
        } catch (Exception e) {
            log.error(String.format("[Expire Hash] batch get hash value failed, cache key: %s, hash keys: %s.",
                    cacheKey, StringUtils.join(hashFields, ",")), e);
        }

        return result;
    }

    /**
     * 扫描缓存
     *
     * @param cacheKey     缓存键
     * @param pageSize     分页大小
     * @param valueHandler 值处理方法
     */
    @Override
    public void hashExpireScan(String cacheKey, int pageSize, Consumer<String> valueHandler) {
        this.hashExpireScan(cacheKey, "*", pageSize, valueHandler);
    }

    /**
     * 扫描缓存
     *
     * @param cacheKey     缓存键
     * @param matchPattern 匹配模式
     * @param pageSize     分页大小
     * @param valueHandler 值处理方法
     */
    @Override
    public void hashExpireScan(String cacheKey, String matchPattern, int pageSize, Consumer<String> valueHandler) {
        this.hashExpireScanAndDealExpiredField(cacheKey, matchPattern, pageSize, valueHandler, null);
    }

    /**
     * 扫描并处理已过期的 Hash 键
     *
     * @param cacheKey       缓存键
     * @param matchPattern   匹配模式
     * @param pageSize       分页大小
     * @param valueHandler   值处理方法
     * @param expiredHandler 过期 Hash 键处理方法（为空时默认清除已过期的 Hash 键）
     */
    @Override
    public void hashExpireScanAndDealExpiredField(
            String cacheKey,
            String matchPattern,
            int pageSize,
            Consumer<String> valueHandler,
            Consumer<List<String>> expiredHandler
    ) {

        // 当前时间
        long currentTime = Instant.now().getEpochSecond();

        // 缓存键后缀
        long cacheKeySuffix = currentTime / Integer.parseInt(DEFAULT_CACHE_KEY_EXPIRE);

        // 当前缓存键
        String currentCacheKey = String.format("%s%d", cacheKey, cacheKeySuffix);

        // 前任缓存键
        String beforeCacheKey = String.format("%s%d", cacheKey, cacheKeySuffix - 1);

        // 扫描当前缓存键
        List<String> expireFields = this.hashExpireScanAndGetExpiredField(currentCacheKey, matchPattern, pageSize, currentTime, valueHandler);

        // 扫描前任缓存键
        expireFields.addAll(this.hashExpireScanAndGetExpiredField(beforeCacheKey, matchPattern, pageSize, currentTime, valueHandler));

        // 处理已过期的 Hash 键
        if (!expireFields.isEmpty()) {

            // 默认处理方法：触发被动检查，删除过期 Hash 键
            if (expiredHandler == null) {
                for (List<String> items : Lists.partition(expireFields, pageSize)) {
                    this.hashExpireBatchGet(cacheKey, items.toArray(new String[0]));
                }
            } else {
                expiredHandler.accept(expireFields);
            }
        }
    }

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
    @Override
    public boolean hashExpireSet(String cacheKey, String hashField, String hashValue, long timeout, TimeUnit unit) {

        // 封装入参
        Map<String, String> hashFieldValues = new HashMap<>();
        hashFieldValues.put(hashField, hashValue);

        // 批量更新
        return this.hashExpireBatchSet(cacheKey, hashFieldValues, timeout, unit) > 0;
    }

    /**
     * 批量更新
     *
     * @param cacheKey        缓存键
     * @param hashFieldValues Hash 键值对
     * @param timeout         过期时间
     * @param unit            单位
     * @return 结果
     */
    @Override
    public int hashExpireBatchSet(String cacheKey, Map<String, String> hashFieldValues, long timeout, TimeUnit unit) {

        // 过期时间（单位：秒）
        long expireSecond = unit.toSeconds(timeout);

        // 封装 Hash 键值对
        Map<String, HashExpireValue> fieldValues = new HashMap<>();
        for (Map.Entry<String, String> entry : hashFieldValues.entrySet()) {
            fieldValues.put(entry.getKey(), new HashExpireValue(expireSecond, entry.getValue()));
        }

        // 批量更新（分页执行）
        return this.hashExpireBatchSetByPage(cacheKey, fieldValues);
    }

    /**
     * 批量删除 HashKey
     *
     * @param cacheKey   缓存键
     * @param hashFields Hash 键集合
     * @return 结果
     */
    @Override
    public int hashExpireRemoveHashKey(String cacheKey, String... hashFields) {
        try {

            // 校验入参
            if (hashFields == null || hashFields.length == 0) {
                return 0;
            }

            // 键集合
            List<String> keys = Collections.singletonList(cacheKey);

            // 参数集合
            List<String> args = new ArrayList<>();

            // 参数 1：缓存键过期时间
            args.add(DEFAULT_CACHE_KEY_EXPIRE);

            // 参数 2：当前时间
            args.add(String.valueOf(Instant.now().getEpochSecond()));

            // 其他参数：HashKey 集合
            args.addAll(Arrays.asList(hashFields));

            // 执行脚本：批量删除 HashKey
            Object luaResult = this.scriptOperations.executeScript(HashExpireRedisScript.singleton().luaBatchDel(), keys, args);

            // 执行结果
            return luaResult == null ? 0 : Integer.parseInt(luaResult.toString());
        } catch (Exception e) {
            log.error(String.format("[Expire Hash] batch delete hash key failed, cache key: %s, hash keys: %s.",
                    cacheKey, StringUtils.join(hashFields, ",")), e);
        }

        return 0;
    }

    /**
     * 删除缓存键
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    @Override
    public int hashExpireRemoveCacheKey(String cacheKey) {
        try {

            // 键集合
            List<String> keys = Collections.singletonList(cacheKey);

            // 参数集合
            List<String> args = new ArrayList<>();

            // 参数 1：缓存键过期时间
            args.add(DEFAULT_CACHE_KEY_EXPIRE);

            // 参数 2：当前时间
            args.add(String.valueOf(Instant.now().getEpochSecond()));

            // 执行脚本：删除缓存键
            Object luaResult = this.scriptOperations.executeScript(HashExpireRedisScript.singleton().luaKeyDel(), keys, args);

            // 执行结果
            return luaResult == null ? 0 : Integer.parseInt(luaResult.toString());
        } catch (Exception e) {
            log.error(String.format("[Expire Hash] delete cache key failed, cache key: %s.", cacheKey), e);
        }

        return 0;
    }

    // endregion

    // region 私有方法

    /**
     * 扫描并返回已过期的 Hash 键
     *
     * @param cacheKey     缓存键
     * @param matchPattern 匹配模式
     * @param pageSize     分页大小
     * @param currentTime  当前时间
     * @param valueHandler 值处理方法
     * @return 已过期的 Hash 键集合
     */
    private List<String> hashExpireScanAndGetExpiredField(
            String cacheKey,
            String matchPattern,
            int pageSize,
            long currentTime,
            Consumer<String> valueHandler
    ) {

        // 已过期的 Hash 键集合
        List<String> expiredFields = new ArrayList<>();

        //生成游标
        try (Cursor<Map.Entry<Object, Object>> cursor = this.getTemplate(cacheKey).opsForHash().scan(cacheKey,
                ScanOptions.scanOptions().match(matchPattern).count(pageSize).build())) {

            // 移动遍历
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> next = cursor.next();
                Object nextValue = next.getValue();
                if (nextValue == null) {
                    continue;
                }

                // 解析 Hash 值
                HashExpireValue expireValue = null;
                try {
                    expireValue = HashExpireValue.analyze(nextValue.toString());
                } catch (Exception e) {
                    log.error("[Expire Hash] hash value analyze failed.", e);
                }

                // 值为空直接返回
                if (expireValue == null) {
                    continue;
                }

                // Hash 键已过期
                if (currentTime > expireValue.getExpire()) {
                    expiredFields.add(next.getKey().toString());
                    continue;
                }

                // Hash 值处理
                valueHandler.accept(expireValue.getRealValue());
            }
        } catch (Exception e) {
            log.error(String.format("[Expire Hash] scan failed, cache key: %s.", cacheKey), e);
        }

        return expiredFields;
    }

    /**
     * 批量更新（分页执行）
     *
     * @param cacheKey        缓存键
     * @param hashFieldValues Hash 键值对
     * @return 结果
     */
    private int hashExpireBatchSetByPage(String cacheKey, Map<String, HashExpireValue> hashFieldValues) {

        // 小于分页大小：直接执行
        if (hashFieldValues.size() <= DEFAULT_BATCH_SET_PAGE_SIZE) {
            return this.hashExpireBatchSetOnSinglePage(cacheKey, hashFieldValues.entrySet());
        }

        // 大于分页大小：分页执行
        int ret = 0;
        List<Map.Entry<String, HashExpireValue>> lst = new ArrayList<>(hashFieldValues.entrySet());
        for (List<Map.Entry<String, HashExpireValue>> partition : Lists.partition(lst, DEFAULT_BATCH_SET_PAGE_SIZE)) {
            ret += this.hashExpireBatchSetOnSinglePage(cacheKey, partition);
        }

        return ret;
    }

    /**
     * 批量更新（单页执行）
     *
     * @param cacheKey        缓存键
     * @param hashFieldValues Hash 键值对
     * @return 结果
     */
    private int hashExpireBatchSetOnSinglePage(String cacheKey, Collection<Map.Entry<String, HashExpireValue>> hashFieldValues) {
        try {

            // 键集合
            List<String> keys = Collections.singletonList(cacheKey);

            // 参数集合
            List<String> args = new ArrayList<>();

            // 参数 1：缓存键过期时间
            args.add(DEFAULT_CACHE_KEY_EXPIRE);

            // 参数 2：当前时间
            args.add(String.valueOf(Instant.now().getEpochSecond()));

            // 其他参数：1：Hash 键、2：Hash 键过期时间、3：版本号、4：真实 Hash 值
            for (Map.Entry<String, HashExpireValue> item : hashFieldValues) {
                args.add(item.getKey());
                args.add(String.valueOf(item.getValue().getExpire()));
                args.add(item.getValue().getVersion());
                args.add(item.getValue().getRealValue());
            }

            // 执行脚本：批量更新
            Object luaResult = this.scriptOperations.executeScript(HashExpireRedisScript.singleton().luaBatchSet(), keys, args);
            if (luaResult == null) {
                return 0;
            }

            return Integer.parseInt(luaResult.toString());
        } catch (Exception e) {
            log.error(String.format("[Expire Hash] batch set hash value failed, cache key: %s, hash keys: %s.",
                    cacheKey, StringUtils.join(hashFieldValues.stream().map(Map.Entry::getKey).collect(Collectors.toList()), ",")), e);
        }

        return 0;
    }

    // endregion
}
