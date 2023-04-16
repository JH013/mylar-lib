package com.mylar.lib.limiter.data;

/**
 * 限流结果
 *
 * @author wangz
 * @date 2023/4/10 0010 0:09
 */
public class RateLimitResult {

    /**
     * 是否允许通过
     */
    private boolean allowed;

    /**
     * 剩余可用凭证数量
     */
    private long remainsPermits;

    /**
     * 允许通过
     *
     * @param remainsPermits 剩余可用凭证数量
     * @return 限流结果
     */
    public static RateLimitResult allow(long remainsPermits) {
        RateLimitResult result = new RateLimitResult();
        result.allowed = true;
        result.remainsPermits = remainsPermits;
        return result;
    }

    /**
     * 禁止通过
     *
     * @return 限流结果
     */
    public static RateLimitResult forbid() {
        RateLimitResult result = new RateLimitResult();
        result.allowed = false;
        result.remainsPermits = 0L;
        return result;
    }

    @Override
    public String toString() {
        return "RateLimitResult{" +
                "allowed=" + allowed +
                ", remainsPermits=" + remainsPermits +
                '}';
    }

    // region getter & setter

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public long getRemainsPermits() {
        return remainsPermits;
    }

    public void setRemainsPermits(long remainsPermits) {
        this.remainsPermits = remainsPermits;
    }
}
