package com.mylar.lib.limiter.plugins.local;

import com.mylar.lib.limiter.core.AbstractRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.args.TokenBucketRateLimitArgs;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 本地-令牌桶限流
 *
 * @author wangz
 * @date 2023/4/16 0016 20:30
 */
public class LocalTokenBucketRateLimiter extends AbstractRateLimiter<TokenBucketRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public LocalTokenBucketRateLimiter(String limitKey, TokenBucketRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);
        this.storedPermits = limitArgs.getBurstCapacity();
    }

    // endregion

    // region 变量

    /**
     * 当前已存储的凭证数量（令牌桶中剩余令牌数量）
     */
    private long storedPermits;

    /**
     * 上次令牌刷新时间
     */
    private long lastRefreshTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

    // endregion

    // region 接口实现

    /**
     * 申请凭证
     *
     * @param requestCount 请求数量
     * @return 限流结果
     */
    @Override
    public synchronized RateLimitResult acquire(int requestCount) {

        // 当前时间
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        // 已度过时间 = 当前时间 - 上次令牌刷新时间
        long spentTime = now - this.lastRefreshTime;
        if (spentTime > 0) {

            // 计算新生成的令牌数量
            long newPermits = spentTime * this.limitArgs.getReplenishRate();

            // 计算当前令牌数量
            this.storedPermits = Math.min(this.storedPermits + newPermits, this.limitArgs.getBurstCapacity());

            // 更新令牌刷新时间
            this.lastRefreshTime = now;
        }

        // 剩余可用凭证数量 = 当前令牌数量 - 请求数量
        long remainsPermits = this.storedPermits - requestCount;

        // 已超限
        if (remainsPermits < 0) {
            return RateLimitResult.forbid();
        }

        // 未超限：更新当前令牌数量
        this.storedPermits -= requestCount;
        return RateLimitResult.allow(remainsPermits);
    }

    // endregion
}