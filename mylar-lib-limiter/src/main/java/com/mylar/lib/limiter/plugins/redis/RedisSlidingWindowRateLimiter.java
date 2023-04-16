package com.mylar.lib.limiter.plugins.redis;

import com.mylar.lib.base.utils.UUIDUtils;
import com.mylar.lib.limiter.core.AbstractRedisRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.args.SlidingWindowRateLimitArgs;
import com.mylar.lib.limiter.script.RedisRateLimiterScript;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Redis-滑动窗口限流
 *
 * @author wangz
 * @date 2023/4/15 0015 2:23
 */
public class RedisSlidingWindowRateLimiter extends AbstractRedisRateLimiter<SlidingWindowRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public RedisSlidingWindowRateLimiter(String limitKey, SlidingWindowRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);
    }

    // endregion

    // region 重写基类方法

    /**
     * 申请凭证
     *
     * @param requestCount 请求数量
     * @return 限流结果
     */
    public RateLimitResult acquire(int requestCount) {

        // Redis 滑动窗口限流不支持请求数量大于 1
        if (requestCount > 1) {
            throw new RuntimeException("Redis sliding window rate limiter is not support request count greater than 1.");
        }

        return super.acquire(requestCount);
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

        // Redis 滑动窗口限流不支持请求数量大于 1
        if (requestCount > 1) {
            throw new RuntimeException("Redis sliding window rate limiter is not support request count greater than 1.");
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
        return RedisRateLimiterScript.singleton().luaSlidingWindowRateLimiter();
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
                String.valueOf(this.limitArgs.getSubTimeCycle()),
                String.valueOf(this.limitArgs.getCycleCount()),
                String.valueOf(this.limitArgs.getCapacity()),
                String.valueOf(Instant.now().getEpochSecond())
        );
    }

    // endregion
}