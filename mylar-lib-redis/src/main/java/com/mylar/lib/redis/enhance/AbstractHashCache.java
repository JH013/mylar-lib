package com.mylar.lib.redis.enhance;

import com.mylar.lib.base.utils.ExtUtils;
import com.mylar.lib.base.utils.JsonUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * HASH缓存抽象基类
 *
 * @author wangz
 * @date 2023/2/26 0026 21:55
 */
public abstract class AbstractHashCache<T> extends AbstractCache implements IHashCache<T> {

    // region 变量

    /**
     * 缓存过期时间最小值（单位：秒）
     */
    protected int minCacheKeyTimeOut = 0;

    /**
     * 缓存过期时间最大值（单位：秒）
     */
    protected int maxCacheKeyTimeOut = 0;

    // endregion

    // region 接口实现

    // region 查询缓存

    /**
     * 查询缓存
     *
     * @param cacheKey  缓存键
     * @param hashField 字段
     * @return 值
     */
    @Override
    public T getCache(String cacheKey, String hashField) {
        return this.redisOperations.opsHash().hashGet(cacheKey, hashField, this.getValueClazz());
    }

    /**
     * 查询缓存
     *
     * @param cacheKey   缓存键
     * @param hashFields 字段集合
     * @return 值集合
     */
    @Override
    public Map<String, T> getCache(String cacheKey, String... hashFields) {
        if (hashFields.length == 0) {
            return new HashMap<>(0);
        }

        Map<String, T> hashValues = new HashMap<>(hashFields.length);
        Map<String, String> tempMap = this.redisOperations.opsHash().hashGetStr(cacheKey, Arrays.asList(hashFields));
        tempMap.forEach((key, value) -> {
            if (key == null) {
                return;
            }

            hashValues.put(key, JsonUtils.deJson(value, this.getValueClazz()));
        });

        return hashValues;
    }

    /**
     * 查询缓存
     *
     * @param cacheKey 缓存键
     * @return 值集合
     */
    @Override
    public Map<String, T> getCache(String cacheKey) {
        return this.redisOperations.opsHash().hashGetAll(cacheKey, this.getValueClazz());
    }

    /**
     * 字段是否存在
     *
     * @param cacheKey  缓存键
     * @param hashField 字段
     * @return 是否存在
     */
    @Override
    public Boolean hashExist(String cacheKey, String hashField) {
        return this.redisOperations.opsHash().hashExist(cacheKey, hashField);
    }

    // endregion

    // region 同步缓存

    /**
     * 同步缓存
     *
     * @param cacheKey  缓存键
     * @param hashField 字段
     * @param hashValue 值
     */
    @Override
    public void syncCache(String cacheKey, String hashField, T hashValue) {

        // 更新缓存
        this.redisOperations.opsHash().hashSyncStr(cacheKey, hashField, JsonUtils.toJson(hashValue));

        // 设置期时间
        this.setExpire(cacheKey);
    }

    /**
     * 同步缓存
     *
     * @param cacheKey   缓存键
     * @param hashValues 值集合
     */
    @Override
    public void syncCache(String cacheKey, Map<String, T> hashValues) {

        // 值集合为空时不更新
        if (hashValues.isEmpty()) {
            return;
        }

        // 转换数据
        Map<String, String> syncItems = new HashMap<>(hashValues.size());
        hashValues.forEach((key, value) -> {
            if (key == null) {
                return;
            }

            syncItems.put(key, JsonUtils.toJson(value));
        });

        // 更新缓存
        this.redisOperations.opsHash().hashSync(cacheKey, syncItems);

        // 设置期时间
        this.setExpire(cacheKey);
    }

    // endregion

    // region 删除缓存

    /**
     * 删除缓存
     *
     * @param cacheKey   缓存键
     * @param hashFields 字段集合
     */
    @Override
    public void removeCache(String cacheKey, String... hashFields) {
        this.redisOperations.opsHash().hashRemove(cacheKey, hashFields);
    }

    // endregion

    // endregion

    // region 私有方法

    /**
     * 设置过期时间
     *
     * @param cacheKey 缓存键
     */
    protected void setExpire(String cacheKey) {
        this.setRandomExpire(cacheKey);
    }

    /**
     * 设置随机过期时间
     *
     * @param cacheKey 缓存键
     */
    protected void setRandomExpire(String cacheKey) {

        // 过期时间全部为0时，默认不过期
        if (this.minCacheKeyTimeOut == 0 && this.maxCacheKeyTimeOut == 0) {
            return;
        }

        int expire;

        // 最小和最大时间相同，则设置固定过期时间
        if (this.minCacheKeyTimeOut == this.maxCacheKeyTimeOut) {
            expire = this.minCacheKeyTimeOut;
        }
        // 最小和最大时间区间随机
        else {
            expire = ExtUtils.randomInt(this.minCacheKeyTimeOut, this.maxCacheKeyTimeOut);
        }

        // 设置过期时间
        this.redisOperations.opsKey().setExpire(cacheKey, expire);
    }

    // endregion
}
