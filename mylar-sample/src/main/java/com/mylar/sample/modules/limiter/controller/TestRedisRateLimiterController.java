package com.mylar.sample.modules.limiter.controller;

import com.mylar.lib.limiter.core.KeyRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.RateLimitStrategyEnum;
import com.mylar.lib.limiter.data.args.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试限流
 *
 * @author wangz
 * @date 2023/4/10 0010 0:12
 */
@RestController
@RequestMapping(value = "/test/limiter/redis")
public class TestRedisRateLimiterController {

    /**
     * fixedWindow
     *
     * @return 结果
     */
    @RequestMapping(value = "/fixedWindow")
    public String fixedWindow() throws Exception {
        RateLimitResult limitResult = KeyRateLimiter.singleton().acquire(
                RateLimitStrategyEnum.REDIS_FIXED_WINDOW,
                "limit.fixed.window",
                FixedWindowRateLimitArgs.create(30, 3),
                1,
                (t) -> System.out.println("Acquire success, " + t),
                (t) -> System.out.println("Acquire failed, " + t)
        );

        return limitResult.toString();
    }

    /**
     * slidingWindow
     *
     * @return 结果
     */
    @RequestMapping(value = "/slidingWindow")
    public String slidingWindow() throws Exception {
        RateLimitResult limitResult = KeyRateLimiter.singleton().acquire(
                RateLimitStrategyEnum.REDIS_SLIDING_WINDOW,
                "limit.sliding.window",
                SlidingWindowRateLimitArgs.create(10, 3, 3),
                1,
                (t) -> System.out.println("Acquire success, " + t),
                (t) -> System.out.println("Acquire failed, " + t)
        );

        return limitResult.toString();
    }

    /**
     * tokenBucket
     *
     * @return 结果
     */
    @RequestMapping(value = "/tokenBucket")
    public String tokenBucket() throws Exception {
        RateLimitResult limitResult = KeyRateLimiter.singleton().acquire(
                RateLimitStrategyEnum.REDIS_TOKEN_BUCKET,
                "limit.token.bucket",
                TokenBucketRateLimitArgs.create(1, 5),
                1,
                (t) -> System.out.println("Acquire success, " + t),
                (t) -> System.out.println("Acquire failed, " + t)
        );

        return limitResult.toString();
    }

    /**
     * leakyBucket
     *
     * @return 结果
     */
    @RequestMapping(value = "/leakyBucket")
    public String leakyBucket() throws Exception {
        RateLimitResult limitResult = KeyRateLimiter.singleton().acquire(
                RateLimitStrategyEnum.REDIS_LEAKY_BUCKET,
                "limit.leaky.bucket",
                LeakyBucketRateLimitArgs.create(1, 5),
                1,
                (t) -> System.out.println("Acquire success, " + t),
                (t) -> System.out.println("Acquire failed, " + t)
        );

        return limitResult.toString();
    }

    /**
     * concurrent
     *
     * @return 结果
     */
    @RequestMapping(value = "/concurrent")
    public String concurrent() throws Exception {
        RateLimitResult limitResult = KeyRateLimiter.singleton().acquire(
                RateLimitStrategyEnum.REDIS_CONCURRENT,
                "limit.concurrent",
                ConcurrentRateLimitArgs.create(1, 120),
                1,
                (t) -> {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    System.out.println("Acquire success, " + t);
                },
                (t) -> System.out.println("Acquire failed, " + t)
        );

        return limitResult.toString();
    }
}
