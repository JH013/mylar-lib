package com.mylar.lib.limiter.plugins.redis;

import com.mylar.lib.base.utils.UUIDUtils;
import com.mylar.lib.limiter.core.AbstractRedisRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.args.ConcurrentRateLimitArgs;
import com.mylar.lib.limiter.script.RedisRateLimiterScript;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Redis-并发限流
 *
 * @author wangz
 * @date 2023/4/15 0015 14:44
 */
public class RedisConcurrentRateLimiter extends AbstractRedisRateLimiter<ConcurrentRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public RedisConcurrentRateLimiter(String limitKey, ConcurrentRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);
    }

    // endregion

    // region 重写基类方法

    /**
     * 申请凭证
     *
     * @return 是否成功
     */
    @Override
    public boolean acquire() {
        throw new RuntimeException("Redis concurrent rate limiter must execute with callback.");
    }

    /**
     * 申请凭证
     *
     * @param requestCount 请求数量
     * @return 限流结果
     */
    @Override
    public RateLimitResult acquire(int requestCount) {
        throw new RuntimeException("Redis concurrent rate limiter must execute with callback.");
    }

    /**
     * 申请凭证
     *
     * @param requestCount    请求数量
     * @param acquiredSuccess 申请成功处理
     * @param acquiredFailed  申请失败处理
     * @return 结果
     */
    @Override
    public RateLimitResult acquire(int requestCount, Consumer<RateLimitResult> acquiredSuccess, Consumer<RateLimitResult> acquiredFailed) {

        // Redis 并发限流不支持请求数量大于 1
        if (requestCount > 1) {
            throw new RuntimeException("Redis concurrent rate limiter is not support request count greater than 1.");
        }

        return super.acquire(requestCount, acquiredSuccess, acquiredFailed);
    }

    /**
     * 获取脚本
     *
     * @return 脚本
     */
    @Override
    protected DefaultRedisScript<?> getScript() {
        return RedisRateLimiterScript.singleton().luaConcurrentRateLimiter();
    }

    /**
     * 获取脚本键集合
     *
     * @return 键集合
     */
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(this.limitKey, UUIDUtils.getInstance().generateShortUuid());
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
                String.valueOf(this.limitArgs.getConcurrentCapacity()),
                String.valueOf(Instant.now().getEpochSecond())
        );
    }

    /**
     * 回调
     *
     * @param keys 键集合
     * @param args 值集合
     */
    @Override
    protected void callback(List<String> keys, List<String> args) {
        this.redisOperations.opsSortSet().sortSetRemove(keys.get(0), Collections.singletonList(keys.get(1)));
    }

    // endregion
}