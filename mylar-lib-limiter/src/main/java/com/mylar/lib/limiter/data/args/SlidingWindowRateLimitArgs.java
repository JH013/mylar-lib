package com.mylar.lib.limiter.data.args;

import com.mylar.lib.limiter.core.IRateLimitArgs;
import com.mylar.lib.limiter.data.RateLimitArgsVerifyResult;

import java.util.Objects;

/**
 * 限流参数-滑动窗口限流
 *
 * @author wangz
 * @date 2023/4/15 0015 13:36
 */
public class SlidingWindowRateLimitArgs implements IRateLimitArgs {

    /**
     * 子限流周期（子窗口大小），单位：秒
     */
    private long subTimeCycle = 1L;

    /**
     * 子限流周期数量（子窗口数量）
     */
    private long cycleCount;

    /**
     * 限流上限
     */
    private long capacity;

    /**
     * 校验限流参数
     */
    @Override
    public RateLimitArgsVerifyResult verify() {
        if (this.subTimeCycle <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Sub time cycle must greater than zero.");
        }

        if (this.cycleCount <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Cycle count must greater than zero.");
        }

        if (this.capacity <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Capacity count must greater than zero.");
        }

        return RateLimitArgsVerifyResult.verifySuccess();
    }

    /**
     * 创建
     *
     * @param cycleCount 子限流周期数量（子窗口数量）
     * @param capacity   限流上限
     */
    public static SlidingWindowRateLimitArgs create(long cycleCount, long capacity) {
        SlidingWindowRateLimitArgs args = new SlidingWindowRateLimitArgs();
        args.cycleCount = cycleCount;
        args.subTimeCycle = 1L;
        args.capacity = capacity;
        return args;
    }

    /**
     * 创建
     *
     * @param subTimeCycle 子限流周期（子窗口大小），单位：秒
     * @param cycleCount   子限流周期数量（子窗口数量）
     * @param capacity     限流上限
     */
    public static SlidingWindowRateLimitArgs create(long subTimeCycle, long cycleCount, long capacity) {
        SlidingWindowRateLimitArgs args = new SlidingWindowRateLimitArgs();
        args.subTimeCycle = subTimeCycle;
        args.cycleCount = cycleCount;
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

        SlidingWindowRateLimitArgs that = (SlidingWindowRateLimitArgs) o;
        return subTimeCycle == that.subTimeCycle && cycleCount == that.cycleCount && capacity == that.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subTimeCycle, cycleCount, capacity);
    }

    // region getter & setter

    public long getSubTimeCycle() {
        return subTimeCycle;
    }

    public void setSubTimeCycle(long subTimeCycle) {
        this.subTimeCycle = subTimeCycle;
    }

    public long getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(long cycleCount) {
        this.cycleCount = cycleCount;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    // endregion
}