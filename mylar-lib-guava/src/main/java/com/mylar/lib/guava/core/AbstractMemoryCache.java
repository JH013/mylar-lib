package com.mylar.lib.guava.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 内存缓存抽象类
 *
 * @author wangz
 * @date 2021/9/25 0025 21:00
 */
public abstract class AbstractMemoryCache<K, V> {

    /**
     * 构造方法
     */
    public AbstractMemoryCache() {
        this.init();
    }

    // region 变量

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(AbstractMemoryCache.class);

    /**
     * 缓存
     */
    protected LoadingCache<K, V> loadingCache;

    // endregion

    // region 公共方法

    /**
     * 查询缓存
     *
     * @param key 键
     * @return 值
     */
    public V get(K key) {
        try {
            return this.loadingCache.get(key);
        } catch (ExecutionException e) {
            logger.error("query guava cache failed", e);
        }

        return null;
    }

    // endregion

    // region 子类重写

    /**
     * 最大缓存项数量
     *
     * @return 最大缓存项数量
     */
    protected long cacheSize() {
        return 10000;
    }

    /**
     * 缓存过期时间
     *
     * @return 缓存过期时间
     */
    protected long expireDuration() {
        return 30;
    }

    /**
     * 缓存过期时间单位
     *
     * @return 缓存过期时间单位
     */
    protected TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

    /**
     * 加载数据
     *
     * @param key 键
     * @return 值
     */
    protected abstract V loadSource(K key);

    // endregion

    // region 私有方法

    /**
     * 初始化
     */
    private void init() {
        this.loadingCache = CacheBuilder.newBuilder()
                .maximumSize(this.cacheSize())
                .expireAfterWrite(this.expireDuration(), this.timeUnit())
                .removalListener(this.removalListener())
                .recordStats()
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K k) {
                        return loadSource(k);
                    }
                });
    }

    /**
     * 移除缓存监听事件
     *
     * @return 监听器
     */
    private RemovalListener<K, V> removalListener() {
        return notification -> logger.info(String.format("memory cache expired item remove, key: %s", notification.getKey()));
    }

    // endregion
}
