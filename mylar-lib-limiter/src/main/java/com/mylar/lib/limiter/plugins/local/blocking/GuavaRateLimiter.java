package com.mylar.lib.limiter.plugins.local.blocking;

import com.google.common.util.concurrent.RateLimiter;
import com.mylar.lib.limiter.core.AbstractRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.args.GuavaRateLimitArgs;

/**
 * 本地-令牌桶限流-阻塞模式
 *
 * @author wangz
 * @date 2023/4/16 0016 22:22
 */
public class GuavaRateLimiter extends AbstractRateLimiter<GuavaRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public GuavaRateLimiter(String limitKey, GuavaRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);

        // 平滑突发限流
        if (limitArgs.getWarmupPeriod() <= 0) {
            this.rateLimiter = RateLimiter.create(limitArgs.getPermitsPerSecond());
        }
        // 平滑预热限流
        else {
            this.rateLimiter = RateLimiter.create(limitArgs.getPermitsPerSecond(), limitArgs.getWarmupPeriod(), limitArgs.getWarmupPeriodUnit());
        }
    }

    // endregion

    // region 变量

    /**
     * 限流器
     */
    private final RateLimiter rateLimiter;

    // endregion

    // region 接口实现

    /**
     * 申请凭证
     *
     * @param requestCount 请求数量
     * @return 限流结果
     */
    @Override
    public RateLimitResult acquire(int requestCount) {

        // 是否申请成功
        boolean acquire;

        // 申请凭证
        if (this.limitArgs.getBlockingTimeout() > 0) {
            acquire = this.rateLimiter.tryAcquire(requestCount, this.limitArgs.getBlockingTimeout(), this.limitArgs.getBlockingTimeoutUnit());
        } else {
            acquire = this.rateLimiter.tryAcquire(requestCount);
        }

        // 申请成功
        if (acquire) {
            return RateLimitResult.allow(1);
        }

        // 申请失败
        return RateLimitResult.forbid();
    }

    // endregion
}