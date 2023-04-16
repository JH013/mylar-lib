package com.mylar.lib.limiter.data.args;

import com.mylar.lib.limiter.core.IRateLimitArgs;
import com.mylar.lib.limiter.data.RateLimitArgsVerifyResult;

import java.util.Objects;

/**
 * 限流参数-令牌桶限流
 *
 * @author wangz
 * @date 2023/4/15 0015 0:32
 */
public class TokenBucketRateLimitArgs implements IRateLimitArgs {

    /**
     * 每秒填充速率（每秒生成多少令牌）
     */
    private long replenishRate;

    /**
     * 限流上限
     */
    private long burstCapacity;

    /**
     * 校验限流参数
     */
    @Override
    public RateLimitArgsVerifyResult verify() {
        if (this.replenishRate <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Replenish rate must greater than zero.");
        }

        if (this.burstCapacity <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Burst capacity must greater than zero.");
        }

        return RateLimitArgsVerifyResult.verifySuccess();
    }

    /**
     * 创建
     *
     * @param replenishRate 每秒填充速率
     * @param burstCapacity 限流上限
     */
    public static TokenBucketRateLimitArgs create(long replenishRate, long burstCapacity) {
        TokenBucketRateLimitArgs args = new TokenBucketRateLimitArgs();
        args.replenishRate = replenishRate;
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

        TokenBucketRateLimitArgs that = (TokenBucketRateLimitArgs) o;
        return replenishRate == that.replenishRate && burstCapacity == that.burstCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(replenishRate, burstCapacity);
    }

    // region getter & setter

    public long getReplenishRate() {
        return replenishRate;
    }

    public void setReplenishRate(long replenishRate) {
        this.replenishRate = replenishRate;
    }

    public long getBurstCapacity() {
        return burstCapacity;
    }

    public void setBurstCapacity(long burstCapacity) {
        this.burstCapacity = burstCapacity;
    }

    // endregion
}
