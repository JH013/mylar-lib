package com.mylar.lib.limiter.plugins.local;

import com.mylar.lib.limiter.core.AbstractRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.args.FixedWindowRateLimitArgs;

/**
 * 本地-固定窗口限流
 *
 * @author wangz
 * @date 2023/4/10 0010 22:02
 */
public class LocalFixedWindowRateLimiter extends AbstractRateLimiter<FixedWindowRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public LocalFixedWindowRateLimiter(String limitKey, FixedWindowRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);
    }

    // endregion

    // region 变量

    /**
     * 上一个窗口的开始时间
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 计数器
     */
    private int counter = 0;

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
        long now = System.currentTimeMillis();

        // 时间窗口过期：重置计数器和时间戳
        if (now - this.timestamp > 1000 * this.limitArgs.getTimeCycle()) {
            this.counter = 0;
            this.timestamp = now;
        }

        // 未超限：计数器累加，返回成功
        if (this.counter + requestCount < this.limitArgs.getCapacity()) {
            this.counter += requestCount;
            return RateLimitResult.allow(this.limitArgs.getCapacity() - this.counter);
        }
        // 已超限：返回失败
        else {
            return RateLimitResult.forbid();
        }
    }

    // endregion
}
