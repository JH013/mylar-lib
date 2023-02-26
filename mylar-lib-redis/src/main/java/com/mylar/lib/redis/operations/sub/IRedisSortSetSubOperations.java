package com.mylar.lib.redis.operations.sub;

import java.util.Map;
import java.util.Set;

/**
 * Redis Sub Operations - Sort Set
 *
 * @author wangz
 * @date 2023/2/26 0026 4:26
 */
public interface IRedisSortSetSubOperations {

    /**
     * 根据 Score 获取缓存值
     *
     * @param cacheKey   缓存键
     * @param startScore 起始 Score（-1/0）
     * @param endScore   截止 Score（1/0）
     * @return 缓存值集合
     */
    <T> Set<T> sortSetGetByScore(String cacheKey, double startScore, double endScore, Class<T> type);

    /**
     * 根据 Index 获取缓存值
     *
     * @param cacheKey   缓存键
     * @param startIndex 起始位置
     * @param endIndex   截止位置（-1 为最后一个，-2 为倒数第二个...）
     * @return 缓存值集合
     */
    <T> Set<T> sortSetGetByIndex(String cacheKey, long startIndex, long endIndex, Class<T> type);

    /**
     * 设置缓存值
     *
     * @param cacheKey                缓存键
     * @param score                   分数
     * @param value                   缓存值
     * @param removeByScoreBeforeSync 同步缓存值前是否先按分数删除旧数据
     * @param timeout                 过期时间（单位：秒）
     */
    <T> void sortSetSync(String cacheKey, double score, T value, boolean removeByScoreBeforeSync, long timeout);

    /**
     * 设置缓存值
     *
     * @param cacheKey                缓存键
     * @param values                  值集合
     * @param removeByScoreBeforeSync 同步缓存值前是否先按分数删除旧数据
     * @param timeout                 过期时间（单位：秒）
     */
    <T> void sortSetSync(String cacheKey, Map<T, Double> values, boolean removeByScoreBeforeSync, long timeout);

    /**
     * 根据 Score 删除缓存值
     *
     * @param cacheKey   缓存键
     * @param startScore 起始 Score
     * @param endScore   截止 Score
     */
    void sortSetRemove(String cacheKey, double startScore, double endScore);

    /**
     * 根据 Score 获取元素数量
     *
     * @param cacheKey   缓存键
     * @param startScore 起始 Score（-1/0）
     * @param endScore   截止 Score（1/0）
     * @return 元素数量
     */
    Long sortSetGetCount(String cacheKey, double startScore, double endScore);

    /**
     * 获取长度
     *
     * @param cacheKey 缓存键
     * @return 长度
     */
    long sortSetGetLength(String cacheKey);
}
