package com.mylar.lib.limiter.data;

import com.alibaba.fastjson.annotation.JSONType;
import com.mylar.lib.base.enums.core.CodeEnum;
import com.mylar.lib.base.enums.core.NameEnum;
import com.mylar.lib.base.enums.data.EnumConvertType;
import com.mylar.lib.base.enums.serialization.EnumCodeValueDeserializer;
import com.mylar.lib.base.enums.utils.EnumConvertCacheUtils;
import com.mylar.lib.limiter.core.IRateLimitArgs;
import com.mylar.lib.limiter.core.IRateLimiter;
import com.mylar.lib.limiter.data.args.*;
import com.mylar.lib.limiter.plugins.local.LocalFixedWindowRateLimiter;
import com.mylar.lib.limiter.plugins.local.LocalLeakyBucketRateLimiter;
import com.mylar.lib.limiter.plugins.local.LocalSlidingWindowRateLimiter;
import com.mylar.lib.limiter.plugins.local.LocalTokenBucketRateLimiter;
import com.mylar.lib.limiter.plugins.local.blocking.GuavaRateLimiter;
import com.mylar.lib.limiter.plugins.local.blocking.LocalBlockingLeakyBucketRateLimiter;
import com.mylar.lib.limiter.plugins.redis.*;

/**
 * 限流策略
 *
 * @author wangz
 * @date 2023/4/16 0016 17:16
 */
@JSONType(deserializer = EnumCodeValueDeserializer.class)
public enum RateLimitStrategyEnum implements CodeEnum, NameEnum {

    /**
     * 本地固定窗口限流
     */
    LOCAL_FIXED_WINDOW("001001", "本地固定窗口限流", LocalFixedWindowRateLimiter.class, FixedWindowRateLimitArgs.class),

    /**
     * 本地滑动窗口限流
     */
    LOCAL_SLIDING_WINDOW("001002", "本地滑动窗口限流", LocalSlidingWindowRateLimiter.class, SlidingWindowRateLimitArgs.class),

    /**
     * 本地漏桶限流
     */
    LOCAL_LEAKY_BUCKET("001003", "本地漏桶限流", LocalLeakyBucketRateLimiter.class, LeakyBucketRateLimitArgs.class),

    /**
     * 本地令牌桶限流
     */
    LOCAL_TOKEN_BUCKET("001004", "本地令牌桶限流", LocalTokenBucketRateLimiter.class, TokenBucketRateLimitArgs.class),

    /**
     * 本地阻塞漏桶限流
     */
    LOCAL_BLOCKING_LEAKEY_BUCKET("003001", "本地阻塞漏桶限流", LocalBlockingLeakyBucketRateLimiter.class, LeakyBucketRateLimitArgs.class),

    /**
     * Guava 限流
     */
    LOCAL_GUAVA("003002", "Guava 限流", GuavaRateLimiter.class, GuavaRateLimitArgs.class),

    /**
     * Redis 固定窗口限流
     */
    REDIS_FIXED_WINDOW("005001", "Redis 固定窗口限流", RedisFixedWindowRateLimiter.class, FixedWindowRateLimitArgs.class),

    /**
     * Redis 滑动窗口限流
     */
    REDIS_SLIDING_WINDOW("005002", "Redis 滑动窗口限流", RedisSlidingWindowRateLimiter.class, SlidingWindowRateLimitArgs.class),

    /**
     * Redis 漏桶限流
     */
    REDIS_LEAKY_BUCKET("005003", "Redis 漏桶限流", RedisLeakBucketRateLimiter.class, LeakyBucketRateLimitArgs.class),

    /**
     * Redis 令牌桶限流
     */
    REDIS_TOKEN_BUCKET("005004", "Redis 令牌桶限流", RedisTokenBucketRateLimiter.class, TokenBucketRateLimitArgs.class),

    /**
     * Redis 并发限流
     */
    REDIS_CONCURRENT("005005", "Redis 并发限流", RedisConcurrentRateLimiter.class, ConcurrentRateLimitArgs.class),

    ;

    /**
     * 构造方法
     *
     * @param code           编码
     * @param name           名称
     * @param limiterClazz   限流器类型
     * @param limitArgsClazz 限流参数类型
     */
    RateLimitStrategyEnum(
            String code,
            String name,
            Class<? extends IRateLimiter> limiterClazz,
            Class<? extends IRateLimitArgs> limitArgsClazz
    ) {
        this.code = code;
        this.name = name;
        this.limiterClazz = limiterClazz;
        this.limitArgsClazz = limitArgsClazz;
    }

    /**
     * 编码
     */
    private final String code;

    /**
     * 名称
     */
    private final String name;

    /**
     * 限流器类型
     */
    private final Class<? extends IRateLimiter> limiterClazz;

    /**
     * 限流参数类型
     */
    private final Class<? extends IRateLimitArgs> limitArgsClazz;

    /**
     * 获取编码
     *
     * @return 编码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 获取限流器类型
     *
     * @return 限流器类型
     */
    public Class<? extends IRateLimiter> getLimiterClazz() {
        return limiterClazz;
    }

    /**
     * 获取限流参数类型
     *
     * @return 限流参数类型
     */
    public Class<? extends IRateLimitArgs> getLimitArgsClazz() {
        return limitArgsClazz;
    }

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static RateLimitStrategyEnum create(Integer value) {
        return EnumConvertCacheUtils.convert(value, RateLimitStrategyEnum.class, EnumConvertType.VALUE);
    }
}
