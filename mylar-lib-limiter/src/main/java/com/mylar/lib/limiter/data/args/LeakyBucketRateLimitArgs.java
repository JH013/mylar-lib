package com.mylar.lib.limiter.data.args;

import com.mylar.lib.limiter.core.IRateLimitArgs;
import com.mylar.lib.limiter.data.RateLimitArgsVerifyResult;

import java.util.Objects;

/**
 * 限流参数-漏桶限流
 *
 * @author wangz
 * @date 2023/4/15 0015 0:32
 */
public class LeakyBucketRateLimitArgs implements IRateLimitArgs {

    /**
     * 每秒漏出速率（每秒漏出多少令牌）
     */
    private long leakRate;

    /**
     * 限流上限
     */
    private long burstCapacity;

    /**
     * 校验限流参数
     */
    @Override
    public RateLimitArgsVerifyResult verify() {
        if (this.leakRate <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("leak rate must greater than zero.");
        }

        if (this.burstCapacity <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Burst capacity must greater than zero.");
        }

        return RateLimitArgsVerifyResult.verifySuccess();
    }

    /**
     * 创建
     *
     * @param leakRate      每秒漏出速率
     * @param burstCapacity 限流上限
     */
    public static LeakyBucketRateLimitArgs create(long leakRate, long burstCapacity) {
        LeakyBucketRateLimitArgs args = new LeakyBucketRateLimitArgs();
        args.leakRate = leakRate;
        args.burstCapacity = burstCapacity;
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

        LeakyBucketRateLimitArgs that = (LeakyBucketRateLimitArgs) o;
        return leakRate == that.leakRate && burstCapacity == that.burstCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leakRate, burstCapacity);
    }

    // region getter & setter

    public long getLeakRate() {
        return leakRate;
    }

    public void setLeakRate(long leakRate) {
        this.leakRate = leakRate;
    }

    public long getBurstCapacity() {
        return burstCapacity;
    }

    public void setBurstCapacity(long burstCapacity) {
        this.burstCapacity = burstCapacity;
    }

    // endregion
}
