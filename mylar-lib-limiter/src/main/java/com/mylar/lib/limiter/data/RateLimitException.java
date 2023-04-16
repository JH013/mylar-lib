package com.mylar.lib.limiter.data;

import com.mylar.lib.limiter.core.IRateLimitArgs;

/**
 * 限流异常
 *
 * @author wangz
 * @date 2023/4/17 0017 0:46
 */
public class RateLimitException extends RuntimeException {

    /**
     * 构造方法
     *
     * @param message 错误信息
     */
    public RateLimitException(String message) {
        super(message);
    }

    /**
     * 检查限流参数是否为空
     *
     * @param limitArgs 限流参数
     */
    public static void checkNull(IRateLimitArgs limitArgs) {
        if (limitArgs == null) {
            throw new RateLimitException("Rate limit args must not be null.");
        }
    }

    /**
     * 校验失败
     *
     * @param message 错误信息
     */
    public static void checkedFailed(String message) {
        throw new RateLimitException(message);
    }
}
