package com.mylar.lib.guava.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.mylar.lib.base.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 内存缓存抽象类
 *
 * @author wangz
 * @date 2021/9/25 0025 21:00
 */
public abstract class AbstractLocalCache<K, V> {

    // region 变量

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(AbstractLocalCache.class);

    /**
     * 缓存
     */
    protected LoadingCache<K, Optional<V>> loadingCache;

    /**
     * 是否已初始化
     */
    private boolean hasInit;

    /**
     * 缓存数量上限，快达到上限或达到上限，时间最长没被访问过的对象被释放
     */
    protected int cacheMaxSize = 1000;

    /**
     * 缓存过期时间，缓存项在写入后，指定时间过期
     */
    protected int expire = 10;

    /**
     * 缓存有效时间的单位（默认：分钟）
     */
    protected TimeUnit timeUnit = TimeUnit.MINUTES;

    // endregion

    // region 公共方法

    /**
     * 优先从缓存读取有效数据，过期时从数据源加载
     *
     * @param key key
     * @return value
     */
    public V getCacheThenSource(K key) {
        try {

            // 初始化
            this.checkInit();

            // 查询数据
            Optional<V> value = this.loadingCache.get(key);
            return value.orElse(null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 设置缓存失效
     *
     * @param keys 缓存键
     */
    public void invalidate(List<K> keys) {
        try {

            // 初始化
            this.checkInit();

            // 失效
            this.loadingCache.invalidateAll(keys);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 设置缓存失效
     *
     * @param keys 缓存键
     */
    public void invalidateKeys(List<?> keys) {
        try {

            // 初始化
            this.checkInit();

            // 失效
            this.loadingCache.invalidateAll(keys);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 设置缓存失效
     */
    public void invalidateAll() {
        try {

            // 初始化
            this.checkInit();

            // 失效
            this.loadingCache.invalidateAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // endregion

    // region 供子类重写

    /**
     * 当缓存不存在时，会调用此函数来加载数据源
     *
     * @param key key
     * @return value
     */
    protected abstract Optional<V> loadSource(K key);

    // endregion

    // region 私有方法

    /**
     * 初始化
     */
    private void checkInit() {

        // 检查是否已经初始化
        if (this.hasInit) {
            return;
        }

        // 初始化缓存
        this.loadingCache = CacheBuilder.newBuilder()
                // 缓存数量上限
                .maximumSize(this.cacheMaxSize)
                // 缓存有效时间
                .expireAfterWrite(this.expire, timeUnit)
                // 缓存移除监听触发
                .removalListener((RemovalListener<K, Optional<V>>) notification -> this.onRemove(notification.getKey(), notification.getValue()))
                // 开启统计功能
                .recordStats()
                .build(new CacheLoader<K, Optional<V>>() {
                    // 数据加载，默认返回-1,也可以是查询操作，如从DB查询
                    @Override
                    public Optional<V> load(K key) {
                        Optional<V> value = loadSource(key);
                        if (log.isDebugEnabled()) {
                            log.debug("Local cache load source, key: {}, value: {}.", key, JsonUtils.toJson(value));
                        }

                        return value;
                    }
                });

        // 标记已初始化
        this.hasInit = true;
    }

    /**
     * 当缓存不存在时，会调用此函数来加载数据源
     *
     * @param key   key
     * @param value value
     */
    protected void onRemove(K key, Optional<V> value) {
        log.debug("Local cache on remove, key: {}, value: {}", key, value.orElse(null));
    }

    // endregion
}
