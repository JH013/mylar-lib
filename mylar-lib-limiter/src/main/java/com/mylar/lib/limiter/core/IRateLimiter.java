package com.mylar.lib.limiter.core;

import com.mylar.lib.limiter.data.RateLimitResult;

import java.util.function.Consumer;

/**
 * 限流器接口
 *
 * @author wangz
 * @date 2023/4/10 0010 21:29
 */
public interface IRateLimiter<LimitArgs extends IRateLimitArgs> {

    /**
     * 申请凭证
     *
     * @return 是否成功
     */
    boolean acquire();

    /**
     * 申请凭证
     *
     * @param requestCount 请求数量
     * @return 限流结果
     */
    RateLimitResult acquire(int requestCount);

    /**
     * 申请凭证
     *
     * @param acquiredSuccess 申请成功处理
     * @param acquiredFailed  申请失败处理
     * @return 结果
     */
    RateLimitResult acquire(Consumer<RateLimitResult> acquiredSuccess, Consumer<RateLimitResult> acquiredFailed);

    /**
     * 申请凭证
     *
     * @param requestCount    请求数量
     * @param acquiredSuccess 申请成功处理
     * @param acquiredFailed  申请失败处理
     * @return 结果
     */
    RateLimitResult acquire(int requestCount, Consumer<RateLimitResult> acquiredSuccess, Consumer<RateLimitResult> acquiredFailed);

    /**
     * 刷新限流参数
     *
     * @param limitArgs 限流参数
     */
    void refreshLimitArgs(LimitArgs limitArgs);
}
