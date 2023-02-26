package com.mylar.lib.redis.operations.sub.impl;

import com.mylar.lib.base.utils.JsonUtils;
import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.operations.sub.IRedisListSubOperations;
import com.mylar.lib.redis.operations.sub.base.AbstractRedisSubOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis Sub Operations - List
 *
 * @author wangz
 * @date 2023/2/26 0026 4:25
 */
public class RedisListSubOperations extends AbstractRedisSubOperations implements IRedisListSubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public RedisListSubOperations(RedisTemplateCache redisTemplateCache) {
        super(redisTemplateCache);
    }

    // region 接口实现

    /**
     * 按范围查询
     *
     * @param cacheKey   缓存键
     * @param startIndex 起始索引
     * @param endIndex   结束索引
     * @return 结果
     */
    @Override
    public List<String> listGetByRange(String cacheKey, long startIndex, long endIndex) {
        return this.getTemplate(cacheKey).opsForList().range(cacheKey, startIndex, endIndex);
    }

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
    @Override
    public <T> List<T> listGetByRange(String cacheKey, long startIndex, long endIndex, Class<T> type) {
        List<String> values = this.getTemplate(cacheKey).opsForList().range(cacheKey, startIndex, endIndex);
        if (CollectionUtils.isEmpty(values)) {
            return new ArrayList<>();
        }

        return values.stream().map(t -> JsonUtils.deJson(t, type)).collect(Collectors.toList());
    }

    /**
     * 按索引查询
     *
     * @param cacheKey 缓存键
     * @param index    索引
     * @param type     类型
     * @param <T>      泛型
     * @return 结果
     */
    @Override
    public <T> T listGetByIndex(String cacheKey, long index, Class<T> type) {
        String value = this.getTemplate(cacheKey).opsForList().index(cacheKey, index);
        return JsonUtils.deJson(value, type);
    }

    /**
     * 查询长度
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    @Override
    public Long listGetSize(String cacheKey) {
        return this.getTemplate(cacheKey).opsForList().size(cacheKey);
    }

    /**
     * 增量同步缓存
     *
     * @param cacheKey 缓存键
     * @param values   缓存值
     * @param leftPush 队首插入
     * @param <T>      泛型
     */
    @Override
    public <T> void listSyncByIncrement(String cacheKey, List<T> values, boolean leftPush) {
        List<String> strValues = values.stream().map(JsonUtils::toJson).collect(Collectors.toList());
        if (leftPush) {
            this.getTemplate(cacheKey).opsForList().leftPushAll(cacheKey, strValues);
        } else {
            this.getTemplate(cacheKey).opsForList().rightPushAll(cacheKey, strValues);
        }
    }

    /**
     * 按索引同步
     *
     * @param cacheKey 缓存键
     * @param index    索引
     * @param value    值
     * @param <T>      泛型
     */
    @Override
    public <T> void listSyncByIndex(String cacheKey, long index, T value) {
        this.getTemplate(cacheKey).opsForList().set(cacheKey, index, JsonUtils.toJson(value));
    }

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
    @Override
    public <T> T listPop(String cacheKey, long timeout, TimeUnit unit, boolean leftPop, Class<T> type) {

        // List Operations
        ListOperations<String, String> listOperations = this.getTemplate(cacheKey).opsForList();

        // 缓存值
        String value;
        if (leftPop) {
            if (timeout > 0 && unit != null) {
                value = listOperations.leftPop(cacheKey);
            } else {
                value = listOperations.leftPop(cacheKey, timeout, unit);
            }
        } else {
            if (timeout > 0 && unit != null) {
                value = listOperations.rightPop(cacheKey);
            } else {
                value = listOperations.rightPop(cacheKey, timeout, unit);
            }
        }

        return JsonUtils.deJson(value, type);
    }

    // endregion
}
