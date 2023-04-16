package com.mylar.lib.limiter.data.args;

import com.mylar.lib.limiter.core.IRateLimitArgs;
import com.mylar.lib.limiter.data.RateLimitArgsVerifyResult;

import java.util.Objects;

/**
 * 限流参数-固定窗口限流
 *
 * @author wangz
 * @date 2023/4/15 0015 0:03
 */
public class FixedWindowRateLimitArgs implements IRateLimitArgs {

    /**
     * 限流周期（窗口大小），单位：秒
     */
    private long timeCycle;

    /**
     * 限流上限
     */
    private long capacity;

    /**
     * 校验限流参数
     */
    @Override
    public RateLimitArgsVerifyResult verify() {
        if (this.timeCycle <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Time cycle must greater than zero.");
        }

        if (this.capacity <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Capacity must greater than zero.");
        }

        return RateLimitArgsVerifyResult.verifySuccess();
    }

    /**
     * 创建
     *
     * @param timeCycle 限流周期（窗口大小），单位：秒
     * @param capacity  限流上限
     */
    public static FixedWindowRateLimitArgs create(long timeCycle, long capacity) {
        FixedWindowRateLimitArgs args = new FixedWindowRateLimitArgs();
        args.timeCycle = timeCycle;
        args.capacity = capacity;
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FixedWindowRateLimitArgs that = (FixedWindowRateLimitArgs) o;
        return timeCycle == that.timeCycle && capacity == that.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeCycle, capacity);
    }

    // region getter & setter

    public long getTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(long timeCycle) {
        this.timeCycle = timeCycle;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    // endregion
}
