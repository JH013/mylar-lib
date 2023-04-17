package com.mylar.sample.modules.limiter.controller;

import com.mylar.lib.limiter.core.KeyRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.RateLimitStrategyEnum;
import com.mylar.lib.limiter.data.args.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试限流
 *
 * @author wangz
 * @date 2023/4/10 0010 0:12
 */
@RestController
@RequestMapping(value = "/test/limiter/local")
public class TestLocalRateLimiterController {

    /**
     * fixedWindow
     *
     * @return 结果
     */
    @RequestMapping(value = "/fixedWindow")
    public String fixedWindow() throws Exception {
        RateLimitResult limitResult = KeyRateLimiter.singleton().acquire(
                RateLimitStrategyEnum.LOCAL_FIXED_WINDOW,
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
                RateLimitStrategyEnum.LOCAL_SLIDING_WINDOW,
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
                RateLimitStrategyEnum.LOCAL_TOKEN_BUCKET,
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
                RateLimitStrategyEnum.LOCAL_LEAKY_BUCKET,
                "limit.leaky.bucket",
                LeakyBucketRateLimitArgs.create(1, 5),
                1,
                (t) -> System.out.println("Acquire success, " + t),
                (t) -> System.out.println("Acquire failed, " + t)
        );

        return limitResult.toString();
    }

    /**
     * guava
     *
     * @return 结果
     */
    @RequestMapping(value = "/guava")
    public String guava() throws Exception {
        RateLimitResult limitResult = KeyRateLimiter.singleton().acquire(
                RateLimitStrategyEnum.LOCAL_GUAVA,
                "limit.guava",
                GuavaRateLimitArgs.create(1),
                1,
                (t) -> System.out.println("Acquire success, " + t),
                (t) -> System.out.println("Acquire failed, " + t)
        );

        return limitResult.toString();
    }

    /**
     * blockingLeakyBucket
     *
     * @return 结果
     */
    @RequestMapping(value = "/blockingLeakyBucket")
    public String blockingLeakyBucket() throws Exception {

        // 多线程执行
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        executorService.submit(getRunnable("thread_1"));
        executorService.submit(getRunnable("thread_2"));
        executorService.submit(getRunnable("thread_3"));
        executorService.submit(getRunnable("thread_4"));
        executorService.submit(getRunnable("thread_5"));
        executorService.submit(getRunnable("thread_6"));
        return "success";
    }

    /**
     * 获取 Runnable
     *
     * @param threadName 线程名
     * @return 结果
     */
    private Runnable getRunnable(String threadName) {
        return () -> {
            while (true) {

                RateLimitResult limitResult = RateLimitResult.forbid();
                try {
                    limitResult = KeyRateLimiter.singleton().acquire(
                            RateLimitStrategyEnum.LOCAL_BLOCKING_LEAKEY_BUCKET,
                            "limit.blocking.leaky.bucket",
                            LeakyBucketRateLimitArgs.create(1, 3),
                            1,
                            (t) -> System.out.println("Acquire success, " + t),
                            (t) -> System.out.println("Acquire failed, " + t)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean acquire = limitResult.isAllowed();
                System.out.println(threadName + " " + LocalDateTime.now().toString() + " acquire " + (acquire ? "success." : "failed."));

                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
