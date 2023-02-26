package com.mylar.lib.redis.operations.sub.impl;

import com.mylar.lib.base.utils.JsonUtils;
import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.operations.sub.IRedisSetSubOperations;
import com.mylar.lib.redis.operations.sub.base.AbstractRedisSubOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Redis Sub Operations - Set
 *
 * @author wangz
 * @date 2023/2/26 0026 4:26
 */
public class RedisSetSubOperations extends AbstractRedisSubOperations implements IRedisSetSubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public RedisSetSubOperations(RedisTemplateCache redisTemplateCache) {
        super(redisTemplateCache);
    }

    // region 接口实现

    /**
     * 弹出一个元素
     *
     * @param cacheKey 缓存键
     * @param type     类型
     * @param <T>      泛型
     * @return 结果
     */
    @Override
    public <T> T setPop(String cacheKey, Class<T> type) {
        String value = this.getTemplate(cacheKey).opsForSet().pop(cacheKey);
        if (!StringUtils.hasLength(value)) {
            return null;
        }

        return JsonUtils.deJson(value, type);
    }

    /**
     * 弹出指定数量元素
     *
     * @param cacheKey 缓存键
     * @param type     类型
     * @param count    数量
     * @param <T>      泛型
     * @return 结果
     */
    @Override
    public <T> List<T> setPopByCount(String cacheKey, Class<T> type, long count) {
        List<String> values = this.getTemplate(cacheKey).opsForSet().pop(cacheKey, count);
        if (CollectionUtils.isEmpty(values)) {
            return new ArrayList<>();
        }

        return values.stream().map(t -> JsonUtils.deJson(t, type)).collect(Collectors.toList());
    }

    /**
     * 同步缓存
     *
     * @param cacheKey 缓存键
     * @param values   缓存值
     * @param <T>      泛型
     */
    @Override
    public <T> void setSync(String cacheKey, List<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return;
        }

        this.getTemplate(cacheKey).opsForSet().add(cacheKey, values.stream().map(JsonUtils::toJson).toArray(String[]::new));
    }

    // endregion
}
