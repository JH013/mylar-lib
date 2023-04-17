package com.mylar.lib.limiter.plugins.local;

import com.mylar.lib.limiter.core.AbstractRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.args.LeakyBucketRateLimitArgs;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 本地-漏桶限流
 *
 * @author wangz
 * @date 2023/4/16 0016 20:30
 */
public class LocalLeakyBucketRateLimiter extends AbstractRateLimiter<LeakyBucketRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public LocalLeakyBucketRateLimiter(String limitKey, LeakyBucketRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);
        this.storedPermits = 0;
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

            // 计算漏出的令牌数量
            long leakedPermits = spentTime * this.limitArgs.getLeakRate();

            // 计算当前令牌数量
            this.storedPermits = Math.max(this.storedPermits - leakedPermits, 0);

            // 更新令牌刷新时间
            this.lastRefreshTime = now;
        }

        // 剩余可用凭证数量 = 限流上限 - 当前令牌数量 - 请求数量
        long remainsPermits = this.limitArgs.getBurstCapacity() - this.storedPermits - requestCount;

        // 已超限
        if (remainsPermits < 0) {
            return RateLimitResult.forbid();
        }

        // 未超限：更新当前令牌数量
        this.storedPermits += requestCount;
        return RateLimitResult.allow(remainsPermits);
    }

    // endregion
}