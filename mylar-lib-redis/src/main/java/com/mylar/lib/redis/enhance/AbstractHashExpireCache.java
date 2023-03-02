package com.mylar.lib.redis.enhance;

import com.mylar.lib.base.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 支持 HashKey 过期的 HASH 缓存
 *
 * @author wangz
 * @date 2023/2/28 0028 23:14
 */
public abstract class AbstractHashExpireCache<T> extends AbstractHashCache<T> implements IHashExpireCache<T> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param cacheKey 缓存键
     */
    public AbstractHashExpireCache(String cacheKey) {

        // 为了支持 Redis 集群的 lua 脚本，必须带 {}
        if (!cacheKey.contains("{") || !cacheKey.contains("}")) {
            this.cacheKey = String.format("{%s}", cacheKey);
        } else {
            this.cacheKey = cacheKey;
        }

        try {
            this.afterPropertiesSet();
        } catch (Exception e) {
            log.error("Init hash cache instance failed.", e);
        }
    }

    // endregion

    // region 变量

    /**
     * 日志
     */
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 缓存键
     */
    protected String cacheKey;

    /**
     * 字段过期时间最小值
     */
    protected long minFieldTimeout = 10;

    /**
     * 字段过期时间最大值
     */
    protected long maxFieldTimeout = 60;

    /**
     * 字段过期时间单位
     */
    protected TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 缺省值
     */
    protected String defaultEmptyHashValue = "$DEFAULT_VALUE$";

    // endregion

    // region 接口实现

    /**
     * 查询缓存
     * <p>
     * 1、单个查询
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @param hashField 字段
     * @return 值
     */
    @Override
    public T getAndSyncIfAbsent(String hashField) {

        // 校验入参
        if (StringUtils.isEmpty(hashField)) {
            return null;
        }

        // 查询缓存
        String hashValueStr = this.redisOperations.opsHashExpire().hashExpireGet(this.cacheKey, hashField);

        // 缓存值为缺省值：返回空
        if (this.defaultEmptyHashValue.equals(hashValueStr)) {
            return null;
        }
        // 缓存值为空：从数据源获取数据
        else if (hashValueStr == null) {

            // 从数据源获取数据
            T source = this.loadSource(hashField);

            // 同步缓存
            this.hashExpireSet(hashField, source);
            return source;
        }
        // 缓存值不为空且不为缺省值：反序列化值并返回
        else {
            return JsonUtils.deJson(hashValueStr, this.getValueClazz());
        }
    }

    /**
     * 查询缓存
     * <p>
     * 1、批量查询
     * 2、缓存不存在时从数据源获取并同步到缓存
     *
     * @param hashFields 字段集合
     * @return 值集合
     */
    @Override
    public Map<String, T> getAndSyncIfAbsent(String... hashFields) {

        // 结果集
        Map<String, T> results = new HashMap<>(hashFields.length);

        // 需要从数据源获取数据的field集合
        List<String> needLoadSourceFields = new ArrayList<>();

        // 批量查询缓存
        Map<String, String> hashValues = this.redisOperations.opsHashExpire().hashExpireBatchGet(this.cacheKey, hashFields);
        for (String hashField : hashFields) {
            String hashValue = hashValues.getOrDefault(hashField, null);

            // 缓存值为缺省值：添加空到结果集中
            if (this.defaultEmptyHashValue.equals(hashValue)) {
                results.put(hashField, null);
            }
            // 缓存值为空：需要从数据源获取数据，暂存到集合中，批量查询后更新缓存
            else if (hashValue == null) {
                needLoadSourceFields.add(hashField);
            }
            // 缓存值不为空且不为缺省值：反序列化值并添加到结果集中
            else {
                results.put(hashField, JsonUtils.deJson(hashValue, this.getValueClazz()));
            }
        }

        // 批量查询数据源，更新缓存，并添加查询结果到结果集中
        if (!CollectionUtils.isEmpty(needLoadSourceFields)) {

            // 从数据源获取数据
            Map<String, T> sources = this.loadSource(needLoadSourceFields);
            for (String hashField : needLoadSourceFields) {

                // 补全数据源查询结果
                if (!sources.containsKey(hashField)) {
                    sources.put(hashField, null);
                }

                // 添加到结果集中
                results.put(hashField, sources.get(hashField));
            }

            // 同步缓存
            this.hashExpireMultiSet(sources);
        }

        return results;
    }

    /**
     * 分页获取某个Hash下所有缓存，并且处理
     *
     * @param pageSize 分页大小，每页scan的数据量
     * @param handler  处理方法
     */
    @Override
    public void scanAll(int pageSize, Consumer<T> handler) {
        this.redisOperations.opsHashExpire().hashExpireScan(this.cacheKey, pageSize, s -> handler.accept(JsonUtils.deJson(s, this.getValueClazz())));
    }

    /**
     * 更新缓存（支持字段过期）
     *
     * @param hashField 字段
     * @param hashValue 值
     * @return 是否成功
     */
    @Override
    public boolean hashExpireSet(String hashField, T hashValue) {
        return this.redisOperations.opsHashExpire().hashExpireSet(cacheKey, hashField, this.formatHashValue(hashValue), this.getExpireTimeOut(), this.timeUnit);
    }

    /**
     * 批量更新缓存（支持字段过期）
     *
     * @param hashValues 值集合
     * @return 成功数量
     */
    @Override
    public int hashExpireMultiSet(Map<String, T> hashValues) {

        // 转换待更新数据
        Map<String, String> syncItems = new HashMap<>(hashValues.size());
        for (Map.Entry<String, T> entry : hashValues.entrySet()) {
            syncItems.put(entry.getKey(), this.formatHashValue(entry.getValue()));
        }

        // 更新缓存
        return this.redisOperations.opsHashExpire().hashExpireBatchSet(this.cacheKey, syncItems, this.getExpireTimeOut(), this.timeUnit);
    }

    /**
     * 批量更新缓存-当HashKey不存在时更新（支持字段过期）
     *
     * @param hashValues 值集合
     * @return 成功数量
     */
    @Override
    public int hashExpireMultiSetNx(Map<String, T> hashValues) {

        // 转换待更新数据
        Map<String, String> syncItems = new HashMap<>(hashValues.size());
        for (Map.Entry<String, T> entry : hashValues.entrySet()) {
            syncItems.put(entry.getKey(), this.formatHashValue(entry.getValue()));
        }

        // 更新缓存
        return this.redisOperations.opsHashExpire().hashExpireBatchSetNx(this.cacheKey, syncItems, this.getExpireTimeOut(), this.timeUnit);
    }

    /**
     * 删除缓存
     *
     * @param hashFields 字段集合
     * @return 删除个数
     */
    @Override
    public int remove(String... hashFields) {
        return this.redisOperations.opsHashExpire().hashExpireRemoveHashKey(this.cacheKey, hashFields);
    }

    /**
     * 删除缓存
     *
     * @return 删除个数
     */
    public int hashExpireRemove() {
        return this.redisOperations.opsHashExpire().hashExpireRemoveCacheKey(this.cacheKey);
    }

    // endregion

    // region 供子类重写

    /**
     * 获取数据源
     *
     * @param hashField 字段
     * @return 值
     */
    protected abstract T loadSource(String hashField);

    /**
     * 批量获取数据源
     *
     * @param hashFields 字段集合
     * @return 值集合
     */
    protected abstract Map<String, T> loadSource(List<String> hashFields);

    /**
     * 获取缓存过期时间
     * <p>
     * 预防雪崩：同步数据源到缓存，同时设置范围的过期时间，比如，过期时间范围为5~10分钟内的随机值
     *
     * @return 缓存过期时间
     */
    protected long getExpireTimeOut() {
        return (long) (Math.random() * (this.maxFieldTimeout - this.minFieldTimeout) + this.minFieldTimeout);
    }

    // endregion

    // region 私有方法

    /**
     * 获取值
     *
     * @param hashValue 原值
     * @return 新值
     */
    private String formatHashValue(T hashValue) {

        // 缓存值为空：设置默认缺省值
        if (hashValue == null) {
            return this.defaultEmptyHashValue;
        }

        return JsonUtils.toJson(hashValue);
    }

    // endregion
}
