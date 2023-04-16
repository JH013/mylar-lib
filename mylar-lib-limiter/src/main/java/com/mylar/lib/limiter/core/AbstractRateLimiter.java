package com.mylar.lib.limiter.core;

import com.mylar.lib.limiter.data.RateLimitArgsVerifyResult;
import com.mylar.lib.limiter.data.RateLimitException;
import com.mylar.lib.limiter.data.RateLimitResult;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 限流器抽象基类
 *
 * @author wangz
 * @date 2023/4/14 0014 23:58
 */
public abstract class AbstractRateLimiter<LimitArgs extends IRateLimitArgs> implements IRateLimiter<LimitArgs> {

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public AbstractRateLimiter(String limitKey, LimitArgs limitArgs) {
        this.limitKey = limitKey;
        this.limitArgs = limitArgs;

        // 校验是否为空
        RateLimitException.checkNull(limitArgs);

        // 校验限流参数
        RateLimitArgsVerifyResult verifyResult = limitArgs.verify();
        if (!verifyResult.isSuccess()) {
            RateLimitException.checkedFailed(verifyResult.getMessage());
        }
    }

    // region 变量

    /**
     * 限流键
     */
    protected String limitKey;

    /**
     * 限流参数
     */
    protected LimitArgs limitArgs;

    // endregion

    // region 接口实现

    /**
     * 申请凭证
     *
     * @return 是否成功
     */
    @Override
    public boolean acquire() {
        return this.acquire(1).isAllowed();
    }

    /**
     * 申请凭证
     *
     * @param acquiredSuccess 申请成功处理
     * @param acquiredFailed  申请失败处理
     * @return 结果
     */
    @Override
    public RateLimitResult acquire(Consumer<RateLimitResult> acquiredSuccess, Consumer<RateLimitResult> acquiredFailed) {
        return this.acquire(1, acquiredSuccess, acquiredFailed);
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

        // 申请凭证
        RateLimitResult limitResult = this.acquire(requestCount);

        // 申请成功
        if (limitResult.isAllowed()) {
            if (acquiredSuccess != null) {
                acquiredSuccess.accept(limitResult);
            }
        }
        // 申请失败
        else {
            if (acquiredFailed != null) {
                acquiredFailed.accept(limitResult);
            }
        }

        // 返回限流结果
        return limitResult;
    }

    /**
     * 刷新限流参数
     *
     * @param limitArgs 限流参数
     */
    @Override
    public void refreshLimitArgs(LimitArgs limitArgs) {

        // 校验是否为空
        RateLimitException.checkNull(limitArgs);

        // 刷新限流参数
        if (!limitArgs.equals(this.limitArgs)) {

            // 校验限流参数
            RateLimitArgsVerifyResult verifyResult = limitArgs.verify();

            // 校验成功：更新限流参数
            if (verifyResult.isSuccess()) {
                this.limitArgs = limitArgs;

            }
            // 校验失败：抛出异常
            else {
                RateLimitException.checkedFailed(verifyResult.getMessage());
            }
        }
    }

    // endregion
}
