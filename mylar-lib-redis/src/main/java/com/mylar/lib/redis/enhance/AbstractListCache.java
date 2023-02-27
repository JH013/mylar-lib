package com.mylar.lib.redis.enhance;

import com.mylar.lib.base.utils.ExtUtils;
import com.mylar.lib.base.utils.JsonUtils;
import com.mylar.lib.redis.script.ListRedisScript;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LIST缓存基类
 *
 * @author wangz
 * @date 2023/2/26 0026 22:28
 */
public abstract class AbstractListCache<T> extends AbstractCache implements IListCache<T> {

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

    /**
     * 查询缓存
     *
     * @param cacheKey 缓存键
     * @param index    索引
     * @return 结果
     */
    @Override
    public T getCache(String cacheKey, long index) {
        return this.redisOperations.opsList().listGetByIndex(cacheKey, index, this.getValueClazz());
    }

    /**
     * 查询缓存（查询全部）
     *
     * @param cacheKey 缓存键
     * @return 结果
     */
    @Override
    public List<T> getCache(String cacheKey) {
        return this.redisOperations.opsList().listGetByRange(cacheKey, 0L, -1L, this.getValueClazz());
    }

    /**
     * 增量同步缓存
     *
     * @param cacheKey 缓存键
     * @param value    值
     */
    @Override
    public void syncCacheIncrement(String cacheKey, T value) {

        // 更新缓存
        this.redisOperations.opsList().listSyncByIncrement(cacheKey, Collections.singletonList(value), false);

        // 设置期时间
        this.setExpire(cacheKey);
    }

    /**
     * 增量同步缓存
     *
     * @param cacheKey 缓存键
     * @param values   值
     */
    @Override
    public void syncCacheIncrement(String cacheKey, List<T> values) {

        // 校验入参
        if (CollectionUtils.isEmpty(values)) {
            return;
        }

        // 更新缓存
        this.redisOperations.opsList().listSyncByIncrement(cacheKey, values, false);

        // 设置期时间
        this.setExpire(cacheKey);
    }

    /**
     * 全量同步缓存
     *
     * @param cacheKey 缓存键
     * @param values   值
     */
    @Override
    public void syncCacheFull(String cacheKey, List<T> values) {

        // 校验入参
        if (CollectionUtils.isEmpty(values)) {
            return;
        }

        // 序列化值集合
        List<String> strValues = values.stream().map(JsonUtils::toJson).collect(Collectors.toList());

        // 全量同步缓存
        this.syncCacheFullWithStringValues(cacheKey, strValues);
    }

    // endregion

    // region 私有方法

    /**
     * 获取过期时间
     *
     * @return 结果
     */
    protected int getExpire() {

        // 过期时间全部为0时，默认不过期
        if (this.minCacheKeyTimeOut == 0 && this.maxCacheKeyTimeOut == 0) {
            return 0;
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

        return expire;
    }

    /**
     * 设置过期时间
     *
     * @param cacheKey 缓存键
     */
    protected void setExpire(String cacheKey) {

        // 获取过期时间
        int expire = this.getExpire();
        if (expire == 0L) {
            return;
        }

        // 设置过期时间
        this.redisOperations.opsKey().setExpire(cacheKey, expire);
    }

    /**
     * 全量同步缓存（String 类型值）
     *
     * @param cacheKey 缓存键
     * @param values   值
     */
    protected void syncCacheFullWithStringValues(String cacheKey, List<String> values) {

        // KEY 集合
        List<String> keyList = new ArrayList<>();
        keyList.add(cacheKey);

        // ARG 集合
        List<String> argList = new ArrayList<>();
        argList.add(String.valueOf(this.getExpire()));
        argList.addAll(values);

        // 执行脚本
        DefaultRedisScript<?> redisScript = ListRedisScript.singleton().luaListSyncFull();
        this.redisOperations.opsScript().executeScript(redisScript, keyList, argList);
    }

    // endregion
}