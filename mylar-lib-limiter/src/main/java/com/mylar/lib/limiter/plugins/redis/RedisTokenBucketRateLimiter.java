package com.mylar.lib.limiter.plugins.redis;

import com.mylar.lib.limiter.core.AbstractRedisRateLimiter;
import com.mylar.lib.limiter.data.args.TokenBucketRateLimitArgs;
import com.mylar.lib.limiter.script.RedisRateLimiterScript;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * Redis-令牌桶限流
 *
 * @author wangz
 * @date 2023/4/15 0015 0:31
 */
public class RedisTokenBucketRateLimiter extends AbstractRedisRateLimiter<TokenBucketRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public RedisTokenBucketRateLimiter(String limitKey, TokenBucketRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);
    }

    // endregion

    // region 重写基类方法

    /**
     * 获取脚本
     *
     * @return 脚本
     */
    @Override
    protected DefaultRedisScript<?> getScript() {
        return RedisRateLimiterScript.singleton().luaTokenBucketRateLimiter();
    }

    /**
     * 获取脚本键集合
     *
     * @return 键集合
     */
    @Override
    protected List<String> getKeys() {

        // 前缀必须带{}，为了集群 Redis key 分配到同一个 slot
        if (this.limitKey.startsWith(CLUSTER_SLOT_PREFIX)) {
            return Arrays.asList(String.format("%s:tokens", this.limitKey), String.format("%s:timestamp", this.limitKey));
        }

        return Arrays.asList(String.format("{%s}:tokens", this.limitKey), String.format("{%s}:timestamp", this.limitKey));
    }

    /**
     * 获取脚本值集合
     *
     * @param requestCount 请求数量
     * @return 值集合
     */
    @Override
    protected List<String> getArgs(int requestCount) {
        return Arrays.asList(
                String.valueOf(this.limitArgs.getReplenishRate()),
                String.valueOf(this.limitArgs.getBurstCapacity()),
                String.valueOf(Instant.now().getEpochSecond()),
                String.valueOf(requestCount)
        );
    }

    // endregion
}
