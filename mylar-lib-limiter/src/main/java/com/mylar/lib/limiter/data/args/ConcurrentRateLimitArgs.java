package com.mylar.lib.limiter.data.args;

import com.mylar.lib.limiter.core.IRateLimitArgs;
import com.mylar.lib.limiter.data.RateLimitArgsVerifyResult;

import java.util.Objects;

/**
 * 限流参数-并发限流
 *
 * @author wangz
 * @date 2023/4/15 0015 14:45
 */
public class ConcurrentRateLimitArgs implements IRateLimitArgs {

    /**
     * 限流上限（并发上限，同一时间最多多少请求处于执行中）
     */
    private long concurrentCapacity;

    /**
     * 超时时间（超时清除限流数据，避免未正常回调而遗留脏数据，单位：秒）
     */
    private long timeout;

    /**
     * 校验限流参数
     */
    @Override
    public RateLimitArgsVerifyResult verify() {
        if (this.concurrentCapacity <= 0) {
            return RateLimitArgsVerifyResult.verifyFailed("Concurrent capacity must greater than zero.");
        }

        return RateLimitArgsVerifyResult.verifySuccess();
    }

    /**
     * 创建
     *
     * @param concurrentCapacity 限流上限（并发上限，同一时间最多多少请求处于执行中）
     * @param timeout            超时时间（超时清除限流数据，避免未正常回调而遗留脏数据，单位：秒）
     */
    public static ConcurrentRateLimitArgs create(long concurrentCapacity, long timeout) {
        ConcurrentRateLimitArgs args = new ConcurrentRateLimitArgs();
        args.concurrentCapacity = concurrentCapacity;
        args.timeout = timeout;
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

        ConcurrentRateLimitArgs that = (ConcurrentRateLimitArgs) o;
        return concurrentCapacity == that.concurrentCapacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(concurrentCapacity);
    }

    // region getter & setter

    public long getConcurrentCapacity() {
        return concurrentCapacity;
    }

    public void setConcurrentCapacity(long concurrentCapacity) {
        this.concurrentCapacity = concurrentCapacity;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    // endregion
}