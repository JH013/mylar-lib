package com.mylar.lib.limiter.core;

import com.mylar.lib.limiter.data.RateLimitException;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.RateLimitStrategyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * 关键字限流器
 *
 * @author wangz
 * @date 2023/4/16 0016 23:00
 */
public class KeyRateLimiter {

    // region 单例

    /**
     * 构造方法
     */
    private KeyRateLimiter() {
    }

    /**
     * 枚举单例
     *
     * @return 单例
     */
    public static KeyRateLimiter singleton() {
        return KeyRateLimiter.SingletonEnum.SINGLETON.instance;
    }

    /**
     * 单例枚举
     */
    private enum SingletonEnum {

        /**
         * 单例
         */
        SINGLETON;

        private final KeyRateLimiter instance;

        SingletonEnum() {
            instance = new KeyRateLimiter();
        }
    }

    // endregion

    // region 变量 & 常量

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(KeyRateLimiter.class);

    /**
     * 限流器缓存容器
     */
    private final RateLimiterContainer containter = new RateLimiterContainer();

    // endregion

    // region 公共方法

    /**
     * 申请凭证
     *
     * @param limitStrategy 限流策略
     * @param limitKey      限流键
     * @param limitArgs     限流参数
     * @param <LimitArgs>   限流参数泛型
     * @return 限流结果
     * @throws Exception 异常
     */
    public <LimitArgs extends IRateLimitArgs> boolean acquire(
            RateLimitStrategyEnum limitStrategy,
            String limitKey,
            LimitArgs limitArgs
    ) throws Exception {
        return this.acquire(limitStrategy, limitKey, limitArgs, 1).isAllowed();
    }

    /**
     * 申请凭证
     *
     * @param limitStrategy 限流策略
     * @param limitKey      限流键
     * @param limitArgs     限流参数
     * @param requestCount  请求数量
     * @param <LimitArgs>   限流参数泛型
     * @return 限流结果
     * @throws Exception 异常
     */
    public <LimitArgs extends IRateLimitArgs> RateLimitResult acquire(
            RateLimitStrategyEnum limitStrategy,
            String limitKey,
            LimitArgs limitArgs,
            int requestCount
    ) throws Exception {
        return this.acquire(limitStrategy, limitKey, limitArgs, requestCount, null, null);
    }

    /**
     * 申请凭证
     *
     * @param limitStrategy   限流策略
     * @param limitKey        限流键
     * @param limitArgs       限流参数
     * @param requestCount    请求数量
     * @param acquiredSuccess 申请成功处理
     * @param acquiredFailed  申请失败处理
     * @param <LimitArgs>     限流参数泛型
     * @return 限流结果
     * @throws Exception 异常
     */
    public <LimitArgs extends IRateLimitArgs> RateLimitResult acquire(
            RateLimitStrategyEnum limitStrategy,
            String limitKey,
            LimitArgs limitArgs,
            int requestCount,
            Consumer<RateLimitResult> acquiredSuccess,
            Consumer<RateLimitResult> acquiredFailed
    ) throws Exception {

        // 校验限流策略是否为空
        if (limitStrategy == null) {
            RateLimitException.checkedFailed("Rate limit strategy must not be null.");
        }

        // 校验限流键是否为空
        if (limitKey == null) {
            RateLimitException.checkedFailed("Rate limit key must not be null.");
        }

        // 校验限流参数是否为空
        RateLimitException.checkNull(limitArgs);

        // 校验限流参数类型
        if (!limitStrategy.getLimitArgsClazz().equals(limitArgs.getClass())) {
            logger.error("Rate limit args type is not match, limit failed.");
            RateLimitException.checkedFailed("Rate limit args type is not match, limit failed.");
        }

        // 创建限流器并刷新限流参数
        IRateLimiter<LimitArgs> rateLimiter = this.createAndRefresh(limitStrategy, limitKey, limitArgs);

        // 申请凭证
        return rateLimiter.acquire(requestCount, acquiredSuccess, acquiredFailed);
    }

    // endregion

    // region 私有方法

    /**
     * 封装唯一键
     *
     * @param limitStrategy 限流策略
     * @param limiterKey    限流键
     * @return 唯一键
     */
    private String formatUniqueKey(RateLimitStrategyEnum limitStrategy, String limiterKey) {
        return String.format("%s-%s", limitStrategy.getCode(), limiterKey);
    }

    /**
     * 创建限流器并刷新限流参数
     *
     * @param limitStrategy 限流策略
     * @param limitKey      限流键
     * @param limitArgs     限流参数
     * @param <LimitArgs>   泛型
     * @return 限流器
     * @throws Exception 异常
     */
    @SuppressWarnings("unchecked")
    private <LimitArgs extends IRateLimitArgs> IRateLimiter<LimitArgs> createAndRefresh(
            RateLimitStrategyEnum limitStrategy,
            String limitKey,
            LimitArgs limitArgs
    ) throws Exception {

        // 封装唯一键
        String uniqueKey = this.formatUniqueKey(limitStrategy, limitKey);

        // 优先从缓存容器中获取
        IRateLimiter<LimitArgs> rateLimiter = (IRateLimiter<LimitArgs>) this.containter.getLimiter(uniqueKey);
        if (rateLimiter == null) {
            synchronized (this) {

                // 创建限流器实例
                rateLimiter = limitStrategy.getLimiterClazz()
                        .getDeclaredConstructor(String.class, limitArgs.getClass())
                        .newInstance(limitKey, limitArgs);

                // 添加到缓存容器
                this.containter.addLimiter(uniqueKey, rateLimiter);
            }
        }

        // 刷新限流参数
        rateLimiter.refreshLimitArgs(limitArgs);
        return rateLimiter;
    }

    // endregion
}
