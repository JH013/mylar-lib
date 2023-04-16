package com.mylar.lib.limiter.script;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Redis 限流脚本
 *
 * @author wangz
 * @date 2023/4/10 0001 0:02
 */
public class RedisRateLimiterScript {

    // region 单例

    /**
     * 构造方法
     */
    private RedisRateLimiterScript() {
    }

    /**
     * 枚举单例
     *
     * @return 单例
     */
    public static RedisRateLimiterScript singleton() {
        return SingletonEnum.SINGLETON.instance;
    }

    /**
     * 单例枚举
     */
    private enum SingletonEnum {

        /**
         * 单例
         */
        SINGLETON;

        private final RedisRateLimiterScript instance;

        SingletonEnum() {
            RedisRateLimiterScript redisScript = new RedisRateLimiterScript();
            redisScript.initAllScript();
            instance = redisScript;
        }
    }

    //endregion

    // region 变量 & 常量

    /**
     * lua 脚本对象集合
     */
    private Map<String, DefaultRedisScript<?>> redisScripts;

    /**
     * lua 脚本路径 - 固定窗口限流
     */
    private static final String LUA_FIXED_WINDOW_RATE_LIMITER = "scripts/fixed_window_request_rate_limiter.lua";

    /**
     * lua 脚本路径 - 滑动窗口限流
     */
    private static final String LUA_SLIDING_WINDOW_RATE_LIMITER = "scripts/sliding_window_request_rate_limiter.lua";

    /**
     * lua 脚本路径 - 令牌桶限流
     */
    private static final String LUA_TOKEN_BUCKET_RATE_LIMITER = "scripts/token_bucket_request_rate_limiter.lua";

    /**
     * lua 脚本路径 - 漏桶限流
     */
    private static final String LUA_LEAKY_BUCKET_RATE_LIMITER = "scripts/leaky_bucket_request_rate_limiter.lua";

    /**
     * lua 脚本路径 - 并发限流
     */
    private static final String LUA_CONCURRENT_RATE_LIMITER = "scripts/concurrent_request_rate_limiter.lua";

    // endregion

    // region 公共方法

    /**
     * lua 脚本 - 固定窗口限流
     */
    public DefaultRedisScript<?> luaFixedWindowRateLimiter() {
        return this.redisScripts.get(LUA_FIXED_WINDOW_RATE_LIMITER);
    }

    /**
     * lua 脚本 - 测试跨脚本函数调用
     */
    public DefaultRedisScript<?> luaSlidingWindowRateLimiter() {
        return this.redisScripts.get(LUA_SLIDING_WINDOW_RATE_LIMITER);
    }

    /**
     * lua 脚本 - 令牌桶限流
     */
    public DefaultRedisScript<?> luaTokenBucketRateLimiter() {
        return this.redisScripts.get(LUA_TOKEN_BUCKET_RATE_LIMITER);
    }

    /**
     * lua 脚本 - 漏桶限流
     */
    public DefaultRedisScript<?> luaLeakyBucketRateLimiter() {
        return this.redisScripts.get(LUA_LEAKY_BUCKET_RATE_LIMITER);
    }

    /**
     * lua 脚本 - 令牌桶限流
     */
    public DefaultRedisScript<?> luaConcurrentRateLimiter() {
        return this.redisScripts.get(LUA_CONCURRENT_RATE_LIMITER);
    }

    // endregion

    // region 私有方法

    /**
     * 初始 lua
     */
    protected void initAllScript() {
        this.redisScripts = new HashMap<>();
        this.initScript(LUA_FIXED_WINDOW_RATE_LIMITER, List.class);
        this.initScript(LUA_SLIDING_WINDOW_RATE_LIMITER, List.class);
        this.initScript(LUA_TOKEN_BUCKET_RATE_LIMITER, List.class);
        this.initScript(LUA_LEAKY_BUCKET_RATE_LIMITER, List.class);
        this.initScript(LUA_CONCURRENT_RATE_LIMITER, List.class);
    }

    /**
     * 初始单个 lua
     */
    protected <T> void initScript(String script, Class<T> resultType) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource(script));
        redisScript.setResultType(resultType);
        redisScripts.put(script, redisScript);
    }

    // endregion
}
