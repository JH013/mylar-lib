package com.mylar.lib.redis.operations.sub.impl;

import com.mylar.lib.base.utils.JsonUtils;
import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.operations.sub.IRedisHashSubOperations;
import com.mylar.lib.redis.operations.sub.base.AbstractRedisSubOperations;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Redis Sub Operations - Hash
 *
 * @author wangz
 * @date 2023/2/26 0026 4:25
 */
public class RedisHashSubOperations extends AbstractRedisSubOperations implements IRedisHashSubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public RedisHashSubOperations(RedisTemplateCache redisTemplateCache) {
        super(redisTemplateCache);
    }

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(RedisHashSubOperations.class);

    // region 接口实现

    /**
     * 获取Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @return Hash值
     */
    @Override
    public String hashGetStr(String cacheKey, String hashKey) {
        Object value = this.getTemplate(cacheKey).opsForHash().get(cacheKey, hashKey);
        if (value == null) {
            return null;
        }

        return value.toString();
    }

    /**
     * 获取Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKeys Hash键集合
     * @return Hash值集合
     */
    @Override
    public Map<String, String> hashGetStr(String cacheKey, List<String> hashKeys) {
        Map<String, String> dic = new HashMap<>();
        List<Object> hashValues = this.getTemplate(cacheKey).opsForHash().multiGet(cacheKey, new ArrayList<>(hashKeys));
        if (hashKeys.size() == hashValues.size()) {
            for (int i = 0; i < hashKeys.size(); i++) {
                Object hashValue = hashValues.get(i);
                dic.put(hashKeys.get(i), hashValue == null ? null : hashValue.toString());
            }
        }

        return dic;
    }

    /**
     * 获取Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @param type     值类型
     * @param <T>      泛型
     * @return Hash值
     */
    @Override
    public <T> T hashGet(String cacheKey, String hashKey, Class<T> type) {
        Object hashValue = this.getTemplate(cacheKey).opsForHash().get(cacheKey, hashKey);
        if (hashValue == null) {
            return null;
        }

        return JsonUtils.deJson(hashValue.toString(), type);
    }

    /**
     * 获取Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKeys Hash键集合
     * @return Hash值集合
     */
    @Override
    public <T> List<T> hashGet(String cacheKey, List<String> hashKeys, Class<T> type) {
        List<Object> hashValues = this.getTemplate(cacheKey).opsForHash().multiGet(cacheKey, new ArrayList<>(hashKeys));
        return hashValues.stream().map(item -> {
            try {
                if (item == null) {
                    return null;
                }

                return JsonUtils.deJson(item.toString(), type);
            } catch (Exception ex) {
                log.error(String.format("Deserialized hash value %s failed, cache key: %s.", item.toString(), cacheKey), ex);
            }

            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 获取全部Hash值
     *
     * @param cacheKey 缓存键
     * @return Hash值集合
     */
    @Override
    public <T> Map<String, T> hashGetAll(String cacheKey, Class<T> type) {
        Map<String, T> dic = new HashMap<>();
        Map<Object, Object> hashValues = this.getTemplate(cacheKey).opsForHash().entries(cacheKey);
        for (Map.Entry<Object, Object> item : hashValues.entrySet()) {
            try {
                if (item.getKey() == null || item.getValue() == null) {
                    continue;
                }

                dic.put(item.getKey().toString(), JsonUtils.deJson(item.getValue().toString(), type));
            } catch (Exception ex) {
                log.error(String.format("Deserialized hash value %s failed, cache key: %s.", item.toString(), cacheKey), ex);
            }
        }

        return dic;
    }

    /**
     * 分页扫描获取Hash值
     *
     * @param cacheKey 缓存键
     * @param pageSize 分页大小
     * @param handler  处理方法
     */
    @Override
    public void hashScanAll(String cacheKey, int pageSize, BiConsumer<String, String> handler) {
        this.hashScanAll(cacheKey, "*", pageSize, handler);
    }

    /**
     * 分页扫描获取Hash值
     *
     * @param cacheKey     缓存键
     * @param matchPattern 匹配模式
     * @param pageSize     分页大小
     * @param handler      处理方法
     */
    @Override
    public void hashScanAll(String cacheKey, String matchPattern, int pageSize, BiConsumer<String, String> handler) {
        try (Cursor<Map.Entry<Object, Object>> cursor = this.getTemplate(cacheKey).opsForHash().scan(cacheKey, ScanOptions.scanOptions().match(matchPattern).count(pageSize).build())) {
            cursor.forEachRemaining(entry -> {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                handler.accept(key, value);
            });
        } catch (Exception e) {
            log.error(String.format("Hash scan failed, cacheKey: %s.", cacheKey), e);
        }
    }

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
    @Override
    public <T> void hashScanAll(String cacheKey, String matchPattern, int pageSize, Class<T> type, Consumer<Map<String, T>> handler) {
        Cursor<Map.Entry<Object, Object>> cursor = null;
        try {
            Map<String, T> values = new HashMap<>();
            cursor = this.getTemplate(cacheKey).opsForHash().scan(cacheKey, ScanOptions.scanOptions().match(matchPattern).count(pageSize).build());
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                if (entry.getKey() == null) {
                    continue;
                }

                values.put(entry.getKey().toString(), entry.getValue() == null ? null : JsonUtils.deJson(entry.getValue().toString(), type));
            }

            handler.accept(values);
        } catch (Exception e) {
            log.error(String.format("Hash scan failed, cacheKey: %s.", cacheKey), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 同步Hash值
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @param value    Hash值
     */
    @Override
    public void hashSyncStr(String cacheKey, String hashKey, String value) {
        this.getTemplate(cacheKey).opsForHash().put(cacheKey, hashKey, value);
    }

    /**
     * 同步Hash值
     *
     * @param cacheKey 缓存键
     * @param values   值集合
     */
    @Override
    public void hashSyncStr(String cacheKey, Map<String, String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }

        this.getTemplate(cacheKey).opsForHash().putAll(cacheKey, values);
    }

    /**
     * 同步Hash值
     *
     * @param cacheKey 缓存键
     * @param values   值集合
     */
    @Override
    public <T> void hashSync(String cacheKey, Map<String, T> values) {
        if (values == null || values.isEmpty()) {
            return;
        }

        // 序列化值
        Map<String, String> beSyncHash = new HashMap<>(values.size());
        for (Map.Entry<String, T> item : values.entrySet()) {
            beSyncHash.put(item.getKey(), JsonUtils.toJson(item.getValue()));
        }

        // 批量同步
        this.getTemplate(cacheKey).opsForHash().putAll(cacheKey, beSyncHash);

        // 释放资源
        beSyncHash.clear();
    }

    /**
     * 批量删除Hash键
     *
     * @param cacheKey 缓存键
     * @param hashKeys Hash键集合
     */
    @Override
    public void hashRemove(String cacheKey, String... hashKeys) {
        if (ArrayUtils.isEmpty(hashKeys)) {
            return;
        }

        this.getTemplate(cacheKey).opsForHash().delete(cacheKey, Arrays.stream(hashKeys).map(t -> (Object) t).toArray());
    }

    /**
     * 获取Hash键数量
     *
     * @param cacheKey 缓存键
     * @return Hash键数量
     */
    @Override
    public Long hashCount(String cacheKey) {
        return this.getTemplate(cacheKey).opsForHash().size(cacheKey);
    }

    /**
     * 获取全部Hash键
     *
     * @param cacheKey 缓存键
     * @return Hash键集合
     */
    @Override
    public Set<String> hashAllKeys(String cacheKey) {
        return this.getTemplate(cacheKey).opsForHash().keys(cacheKey).stream().map(Object::toString).collect(Collectors.toSet());
    }

    /**
     * Hash键是否存在
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @return 是否存在
     */
    @Override
    public boolean hashExist(String cacheKey, String hashKey) {
        return Boolean.TRUE.equals(this.getTemplate(cacheKey).opsForHash().hasKey(cacheKey, hashKey));
    }

    /**
     * 自增
     *
     * @param cacheKey 缓存键
     * @param hashKey  Hash键
     * @param value    计数
     * @return 当前计数
     */
    @Override
    public long hashIncrement(String cacheKey, String hashKey, long value) {
        return this.getTemplate(cacheKey).opsForHash().increment(cacheKey, hashKey, value);
    }

    // endregion
}
