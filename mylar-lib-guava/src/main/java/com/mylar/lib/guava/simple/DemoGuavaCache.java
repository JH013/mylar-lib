package com.mylar.lib.guava.simple;

import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * guava cache demo
 *
 * @author wangz
 * @date 2021/9/25 0025 19:32
 */
public class DemoGuavaCache {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(DemoGuavaCache.class);

    /**
     * 缓存项最大数量
     */
    private static final long GUAVA_CACHE_SIZE = 100000;

    /**
     * 缓存时间
     */
    private static final long GUAVA_CACHE_DURATION = 30;

    /**
     * 缓存操作对象
     */
    private static LoadingCache<String, String> GLOBAL_CACHE = null;

    /**
     * 构造方法
     */
    static {
        try {
            GLOBAL_CACHE = CacheBuilder.newBuilder()
                    // 缓存池大小，在缓存项接近该大小时，Guava开始回收旧的缓存项
                    .maximumSize(GUAVA_CACHE_SIZE)
                    // 设置时间对象没有被读/写访问则对象从内存中删除
                    .expireAfterAccess(GUAVA_CACHE_DURATION, TimeUnit.SECONDS)
                    // 设置缓存在写入之后，设定时间后失效
                    .expireAfterWrite(GUAVA_CACHE_DURATION, TimeUnit.SECONDS)
                    // 移除监听器，缓存项被移除时会触发
                    .removalListener((RemovalListener<String, String>) removalNotification ->
                            logger.info(String.format("remove cache, key: %s", removalNotification.getKey())))
                    // 开启统计功能
                    .recordStats()
                    // 缓存项不存在时，加载逻辑
                    .build(new CacheLoader<String, String>() {
                        @Override
                        public String load(String key) {
                            logger.info(String.format("load from other source, key: %s", key));
                            return key + "_" + UUID.randomUUID().toString();
                        }
                    });
        } catch (Exception e) {
            logger.error("init guava cache failed", e);
        }
    }

    /**
     * 查询
     *
     * @param key key
     * @return value
     */
    public static String get(String key) {
        try {
            return GLOBAL_CACHE.get(key);
        } catch (ExecutionException e) {
            logger.error("query guava cache failed", e);
        }

        return null;
    }
}