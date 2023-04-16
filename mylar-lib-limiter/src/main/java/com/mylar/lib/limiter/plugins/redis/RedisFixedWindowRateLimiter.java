package com.mylar.lib.limiter.plugins.redis;

import com.mylar.lib.limiter.core.AbstractRedisRateLimiter;
import com.mylar.lib.limiter.data.args.FixedWindowRateLimitArgs;
import com.mylar.lib.limiter.script.RedisRateLimiterScript;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Redis-固定窗口限流
 *
 * @author wangz
 * @date 2023/4/10 0010 0:07
 */
public class RedisFixedWindowRateLimiter extends AbstractRedisRateLimiter<FixedWindowRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public RedisFixedWindowRateLimiter(String limitKey, FixedWindowRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);
    }

    // endregion

    // region 重写基类方法

    /**
     * 获取脚本
     *
     * @return 脚本
     */
    @Override
    protected DefaultRedisScript<?> getScript() {
        return RedisRateLimiterScript.singleton().luaFixedWindowRateLimiter();
    }

    /**
     * 获取脚本键集合
     *
     * @return 键集合
     */
    @Override
    protected List<String> getKeys() {
        return Collections.singletonList(this.limitKey);
    }

    /**
     * 获取脚本值集合
     *
     * @param requestCount 请求数量
     * @return 值集合
     */
    @Override
    protected List<String> getArgs(int requestCount) {
        return Arrays.asList(
                String.valueOf(this.limitArgs.getTimeCycle()),
                String.valueOf(this.limitArgs.getCapacity()),
                String.valueOf(requestCount)
        );
    }

    // endregion
}