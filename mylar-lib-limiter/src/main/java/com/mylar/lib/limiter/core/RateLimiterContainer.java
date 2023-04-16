package com.mylar.lib.limiter.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 限流器容器
 *
 * @author wangz
 * @date 2023/4/17 0017 0:08
 */
public class RateLimiterContainer {

    // region 构造方法

    /**
     * 构造方法
     */
    public RateLimiterContainer() {
        this.limiterCache = new HashMap<>();
    }

    // endregion

    // region 变量

    /**
     * 限流器实例缓存
     */
    private final Map<String, IRateLimiter<?>> limiterCache;

    // endregion

    // region 公共方法

    /**
     * 获取限流器
     *
     * @param uniqueKey 唯一键
     * @return 实例
     */
    public IRateLimiter<?> getLimiter(String uniqueKey) {
        return this.limiterCache.get(uniqueKey);
    }

    /**
     * 添加限流器
     *
     * @param uniqueKey   唯一键
     * @param rateLimiter 限流器
     */
    public void addLimiter(String uniqueKey, IRateLimiter<?> rateLimiter) {
        this.limiterCache.put(uniqueKey, rateLimiter);
    }

    // endregion
}
