package com.mylar.lib.limiter.core;

import com.mylar.lib.limiter.data.RateLimitArgsVerifyResult;

/**
 * 限流参数接口
 *
 * @author wangz
 * @date 2023/4/10 0010 22:06
 */
public interface IRateLimitArgs {

    /**
     * 校验限流参数
     */
    RateLimitArgsVerifyResult verify();
}
