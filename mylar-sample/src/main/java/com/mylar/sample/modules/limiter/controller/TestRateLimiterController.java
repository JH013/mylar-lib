package com.mylar.sample.modules.limiter.controller;

import com.mylar.lib.limiter.core.KeyRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.RateLimitStrategyEnum;
import com.mylar.lib.limiter.data.args.ConcurrentRateLimitArgs;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangz
 * @date 2023/4/10 0010 0:12
 */
@RestController
@RequestMapping(value = "/test/limiter")
public class TestRateLimiterController {

    /**
     * redisConcurrent
     *
     * @return 结果
     */
    @RequestMapping(value = "/redisConcurrent")
    public String redisConcurrent() throws Exception {

        RateLimitResult limitResult = KeyRateLimiter.singleton().acquire(
                RateLimitStrategyEnum.REDIS_CONCURRENT,
                "test_concurrent",
                ConcurrentRateLimitArgs.create(3),
                1,
                (t) -> System.out.println("Acquire success, " + t),
                (t) -> System.out.println("Acquire failed, " + t)
        );

        return limitResult.toString();
    }
}
