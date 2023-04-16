package com.mylar.lib.limiter.data.args;

import com.mylar.lib.limiter.core.IRateLimitArgs;
import com.mylar.lib.limiter.data.RateLimitArgsVerifyResult;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 限流参数-Guava 限流
 *
 * @author wangz
 * @date 2023/4/16 0016 22:26
 */
public class GuavaRateLimitArgs implements IRateLimitArgs {

    /**
     * 每秒生成令牌数量
     */
    private double permitsPerSecond;

    /**
     * 预热时间
     */
    private long warmupPeriod;

    /**
     * 预热时间单位
     */
    private TimeUnit warmupPeriodUnit;

    /**
     * 阻塞超时时间
     */
    private long blockingTimeout;

    /**
     * 阻塞超时时间单位
     */
    private TimeUnit blockingTimeoutUnit;

    /**
     * 校验限流参数
     */
    @Override
    public RateLimitArgsVerifyResult verify() {
        if (this.permitsPerSecond <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Permits per second must greater than zero.");
        }

        if (this.warmupPeriod > 0 && this.warmupPeriodUnit == null) {
            return RateLimitArgsVerifyResult.verifyFailed("Warmup period unit must not be null.");
        }

        if (this.blockingTimeout > 0 && this.blockingTimeoutUnit == null) {
            return RateLimitArgsVerifyResult.verifyFailed("Blocking timeout unit must greater than zero.");
        }

        return RateLimitArgsVerifyResult.verifySuccess();
    }

    /**
     * 创建
     *
     * @param permitsPerSecond 每秒生成令牌数量
     */
    public static GuavaRateLimitArgs create(double permitsPerSecond) {
        GuavaRateLimitArgs args = new GuavaRateLimitArgs();
        args.permitsPerSecond = permitsPerSecond;
        args.blockingTimeout = 0L;
        args.blockingTimeoutUnit = TimeUnit.MILLISECONDS;
        return args;
    }

    /**
     * 创建
     *
     * @param permitsPerSecond    每秒生成令牌数量
     * @param blockingTimeout     阻塞超时时间
     * @param blockingTimeoutUnit 阻塞超时时间单位
     */
    public static GuavaRateLimitArgs create(double permitsPerSecond, long blockingTimeout, TimeUnit blockingTimeoutUnit) {
        GuavaRateLimitArgs args = new GuavaRateLimitArgs();
        args.permitsPerSecond = permitsPerSecond;
        args.blockingTimeout = blockingTimeout;
        args.blockingTimeoutUnit = blockingTimeoutUnit;
        return args;
    }

    /**
     * 创建
     *
     * @param permitsPerSecond 每秒生成令牌数量
     * @param warmupPeriod     预热时间
     * @param warmupPeriodUnit 预热时间单位
     */
    public static GuavaRateLimitArgs createWarmup(double permitsPerSecond, long warmupPeriod, TimeUnit warmupPeriodUnit) {
        GuavaRateLimitArgs args = new GuavaRateLimitArgs();
        args.permitsPerSecond = permitsPerSecond;
        args.warmupPeriod = warmupPeriod;
        args.warmupPeriodUnit = warmupPeriodUnit;
        args.blockingTimeout = 0L;
        args.blockingTimeoutUnit = TimeUnit.MILLISECONDS;
        return args;
    }

    /**
     * 创建
     *
     * @param permitsPerSecond    每秒生成令牌数量
     * @param warmupPeriod        预热时间
     * @param warmupPeriodUnit    预热时间单位
     * @param blockingTimeout     阻塞超时时间
     * @param blockingTimeoutUnit 阻塞超时时间单位
     */
    public static GuavaRateLimitArgs createWarmup(
            double permitsPerSecond,
            long warmupPeriod,
            TimeUnit warmupPeriodUnit,
            long blockingTimeout,
            TimeUnit blockingTimeoutUnit
    ) {
        GuavaRateLimitArgs args = new GuavaRateLimitArgs();
        args.permitsPerSecond = permitsPerSecond;
        args.warmupPeriod = warmupPeriod;
        args.warmupPeriodUnit = warmupPeriodUnit;
        args.blockingTimeout = blockingTimeout;
        args.blockingTimeoutUnit = blockingTimeoutUnit;
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

        GuavaRateLimitArgs that = (GuavaRateLimitArgs) o;
        return Double.compare(that.permitsPerSecond, permitsPerSecond) == 0
                && warmupPeriod == that.warmupPeriod
                && blockingTimeout == that.blockingTimeout &&
                warmupPeriodUnit == that.warmupPeriodUnit
                && blockingTimeoutUnit == that.blockingTimeoutUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(permitsPerSecond, warmupPeriod, warmupPeriodUnit, blockingTimeout, blockingTimeoutUnit);
    }

    // region getter & setter

    public double getPermitsPerSecond() {
        return permitsPerSecond;
    }

    public void setPermitsPerSecond(double permitsPerSecond) {
        this.permitsPerSecond = permitsPerSecond;
    }

    public long getWarmupPeriod() {
        return warmupPeriod;
    }

    public void setWarmupPeriod(long warmupPeriod) {
        this.warmupPeriod = warmupPeriod;
    }

    public TimeUnit getWarmupPeriodUnit() {
        return warmupPeriodUnit;
    }

    public void setWarmupPeriodUnit(TimeUnit warmupPeriodUnit) {
        this.warmupPeriodUnit = warmupPeriodUnit;
    }

    public long getBlockingTimeout() {
        return blockingTimeout;
    }

    public void setBlockingTimeout(long blockingTimeout) {
        this.blockingTimeout = blockingTimeout;
    }

    public TimeUnit getBlockingTimeoutUnit() {
        return blockingTimeoutUnit;
    }

    public void setBlockingTimeoutUnit(TimeUnit blockingTimeoutUnit) {
        this.blockingTimeoutUnit = blockingTimeoutUnit;
    }

    // endregion
}
