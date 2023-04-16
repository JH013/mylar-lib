package com.mylar.lib.limiter.core;

import com.mylar.lib.base.enhance.SpringResolver;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.redis.operations.IRedisAggregateOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Redis 限流器抽象基类
 *
 * @author wangz
 * @date 2023/4/15 0015 0:42
 */
public abstract class AbstractRedisRateLimiter<LimitArgs extends IRateLimitArgs> extends AbstractRateLimiter<LimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public AbstractRedisRateLimiter(String limitKey, LimitArgs limitArgs) {
        super(limitKey, limitArgs);
        this.redisOperations = SpringResolver.resolve(IRedisAggregateOperations.class);
    }

    // endregion

    // region 变量 & 常量

    /**
     * 集群同一哈希槽的前缀
     */
    protected static final String CLUSTER_SLOT_PREFIX = "{";

    /**
     * Redis Operations
     */
    protected final IRedisAggregateOperations redisOperations;

    // endregion

    // region 接口实现

    /**
     * 申请凭证
     *
     * @return 是否成功
     */
    @Override
    public boolean acquire() {
        return this.acquire(1).isAllowed();
    }

    /**
     * 申请凭证
     *
     * @param requestCount 请求数量
     * @return 限流结果
     */
    @Override
    public RateLimitResult acquire(int requestCount) {
        return this.acquire(this.getScript(), this.getKeys(), this.getArgs(requestCount));
    }

    /**
     * 申请凭证
     *
     * @param acquiredSuccess 申请成功处理
     * @param acquiredFailed  申请失败处理
     * @return 结果
     */
    @Override
    public RateLimitResult acquire(Consumer<RateLimitResult> acquiredSuccess, Consumer<RateLimitResult> acquiredFailed) {
        return this.acquire(1, acquiredSuccess, acquiredFailed);
    }

    /**
     * 申请凭证
     *
     * @param requestCount    请求数量
     * @param acquiredSuccess 申请成功处理
     * @param acquiredFailed  申请失败处理
     * @return 结果
     */
    @Override
    public RateLimitResult acquire(int requestCount, Consumer<RateLimitResult> acquiredSuccess, Consumer<RateLimitResult> acquiredFailed) {

        // 限流结果：默认不限流
        RateLimitResult limitResult = RateLimitResult.allow(-1);

        // 键集合
        List<String> keys = this.getKeys();

        // 值集合
        List<String> args = this.getArgs(requestCount);
        try {

            // 申请凭证
            limitResult = this.acquire(this.getScript(), keys, args);

            // 申请成功
            if (limitResult.isAllowed()) {
                if (acquiredSuccess != null) {
                    acquiredSuccess.accept(limitResult);
                }
            }
            // 申请失败
            else {
                if (acquiredFailed != null) {
                    acquiredFailed.accept(limitResult);
                }
            }
        } finally {

            // 执行回调
            if (limitResult.isAllowed() && limitResult.getRemainsPermits() != -1L) {
                this.callback(keys, args);
            }
        }

        // 返回限流结果
        return limitResult;
    }

    // endregion

    // region 供子类重写

    /**
     * 获取脚本
     *
     * @return 脚本
     */
    protected abstract DefaultRedisScript<?> getScript();

    /**
     * 获取脚本键集合
     *
     * @return 键集合
     */
    protected abstract List<String> getKeys();

    /**
     * 获取脚本值集合
     *
     * @param requestCount 请求数量
     * @return 值集合
     */
    protected abstract List<String> getArgs(int requestCount);

    /**
     * 回调
     *
     * @param keys 键集合
     * @param args 值集合
     */
    protected void callback(List<String> keys, List<String> args) {

    }

    // endregion

    // region 私有方法

    /**
     * 申请凭证
     *
     * @param script 脚本
     * @param keys   键集合
     * @param args   值集合
     * @return 结果
     */
    private RateLimitResult acquire(DefaultRedisScript<?> script, List<String> keys, List<String> args) {

        // 执行脚本
        Object luaResult = redisOperations.opsScript().executeScript(script, keys, args);

        // 执行结果
        List<?> lst = (List<?>) luaResult;
        if (lst != null && lst.size() == 2) {
            long allowedCount = Long.parseLong(lst.get(0).toString());
            long remainsCount = Long.parseLong(lst.get(1).toString());
            return allowedCount > 0 ? RateLimitResult.allow(remainsCount) : RateLimitResult.forbid();
        }

        // 默认不限流
        return RateLimitResult.allow(-1);
    }

    // endregion
}
