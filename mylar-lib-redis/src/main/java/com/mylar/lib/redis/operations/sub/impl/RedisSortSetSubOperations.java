package com.mylar.lib.redis.operations.sub.impl;

import com.mylar.lib.base.utils.JsonUtils;
import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.operations.sub.IRedisSortSetSubOperations;
import com.mylar.lib.redis.operations.sub.base.AbstractRedisSubOperations;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis Sub Operations - Sort Set
 *
 * @author wangz
 * @date 2023/2/26 0026 4:26
 */
public class RedisSortSetSubOperations extends AbstractRedisSubOperations implements IRedisSortSetSubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public RedisSortSetSubOperations(RedisTemplateCache redisTemplateCache) {
        super(redisTemplateCache);
    }

    // region 接口实现

    /**
     * 根据 Score 获取缓存值
     *
     * @param cacheKey   缓存键
     * @param startScore 起始 Score（-1/0）
     * @param endScore   截止 Score（1/0）
     * @return 缓存值集合
     */
    @Override
    public <T> Set<T> sortSetGetByScore(String cacheKey, double startScore, double endScore, Class<T> type) {
        Set<String> items = this.getTemplate(cacheKey).opsForZSet().rangeByScore(cacheKey, startScore, endScore);
        if (items == null || items.isEmpty()) {
            return new LinkedHashSet<>();
        }

        return items.stream().map(t -> JsonUtils.deJson(t, type)).collect(Collectors.toSet());
    }

    /**
     * 根据 Index 获取缓存值
     *
     * @param cacheKey   缓存键
     * @param startIndex 起始位置
     * @param endIndex   截止位置（-1 为最后一个，-2 为倒数第二个...）
     * @return 缓存值集合
     */
    @Override
    public <T> Set<T> sortSetGetByIndex(String cacheKey, long startIndex, long endIndex, Class<T> type) {
        Set<String> items = this.getTemplate(cacheKey).opsForZSet().range(cacheKey, startIndex, endIndex);
        if (items == null || items.isEmpty()) {
            return new LinkedHashSet<>();
        }

        return items.stream().map(t -> JsonUtils.deJson(t, type)).collect(Collectors.toSet());
    }

    /**
     * 设置缓存值
     *
     * @param cacheKey                缓存键
     * @param score                   分数
     * @param value                   缓存值
     * @param removeByScoreBeforeSync 同步缓存值前是否先按分数删除旧数据
     * @param timeout                 过期时间（单位：秒）
     */
    @Override
    public <T> void sortSetSync(String cacheKey, double score, T value, boolean removeByScoreBeforeSync, long timeout) {

        // RedisTemplate
        StringRedisTemplate redisTemplate = this.getTemplate(cacheKey);

        // SortSet Operations
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();

        // 缓存键是否存在
        boolean exist = true;
        if (timeout > 0) {
            exist = Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));
        }

        // 根据 Score 删除旧数据
        if (removeByScoreBeforeSync) {
            opsForZSet.removeRangeByScore(cacheKey, score, score);
        }

        // 添加新数据
        opsForZSet.add(cacheKey, JsonUtils.toJson(value), score);

        // 第一次添加缓存键时设置过期时间
        if (timeout > 0 && !exist) {
            redisTemplate.expire(cacheKey, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置缓存值
     *
     * @param cacheKey                缓存键
     * @param values                  值集合
     * @param removeByScoreBeforeSync 同步缓存值前是否先按分数删除旧数据
     * @param timeout                 过期时间（单位：秒）
     */
    @Override
    public <T> void sortSetSync(String cacheKey, Map<T, Double> values, boolean removeByScoreBeforeSync, long timeout) {

        // RedisTemplate
        StringRedisTemplate redisTemplate = this.getTemplate(cacheKey);

        // SortSet Operations
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();

        // 缓存键是否存在
        boolean exist = true;
        if (timeout > 0) {
            exist = Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));
        }

        // 封装待添加数据
        Set<ZSetOperations.TypedTuple<String>> items = new HashSet<>();
        for (Map.Entry<T, Double> entry : values.entrySet()) {

            // 根据 Score 删除旧数据
            if (removeByScoreBeforeSync) {
                opsForZSet.removeRangeByScore(cacheKey, entry.getValue(), entry.getValue());
            }

            items.add(new DefaultTypedTuple<>(JsonUtils.toJson(entry.getKey()), entry.getValue()));
        }

        // 批量添加新数据
        opsForZSet.add(cacheKey, items);

        // 第一次添加缓存键时设置过期时间
        if (timeout > 0 && !exist) {
            redisTemplate.expire(cacheKey, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据 Score 删除缓存值
     *
     * @param cacheKey   缓存键
     * @param startScore 起始 Score
     * @param endScore   截止 Score
     */
    @Override
    public void sortSetRemove(String cacheKey, double startScore, double endScore) {
        this.getTemplate(cacheKey).opsForZSet().removeRangeByScore(cacheKey, startScore, endScore);
    }

    /**
     * 根据 Score 删除缓存值
     *
     * @param cacheKey 缓存键
     * @param values   值集合
     */
    @Override
    public <T> void sortSetRemove(String cacheKey, List<T> values) {
        this.getTemplate(cacheKey).opsForZSet().remove(cacheKey, values);
    }

    /**
     * 根据 Score 获取元素数量
     *
     * @param cacheKey   缓存键
     * @param startScore 起始 Score（-1/0）
     * @param endScore   截止 Score（1/0）
     * @return 元素数量
     */
    @Override
    public Long sortSetGetCount(String cacheKey, double startScore, double endScore) {
        return this.getTemplate(cacheKey).opsForZSet().count(cacheKey, startScore, endScore);
    }

    /**
     * 获取长度
     *
     * @param cacheKey 缓存键
     * @return 长度
     */
    @Override
    public long sortSetGetLength(String cacheKey) {
        Long size = this.getTemplate(cacheKey).opsForSet().size(cacheKey);
        return size == null ? 0L : size;
    }

    // endregion
}
