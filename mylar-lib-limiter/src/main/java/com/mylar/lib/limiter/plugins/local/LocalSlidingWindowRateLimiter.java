package com.mylar.lib.limiter.plugins.local;

import com.mylar.lib.limiter.core.AbstractRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.args.SlidingWindowRateLimitArgs;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 本地-滑动窗口限流
 *
 * @author wangz
 * @date 2023/4/16 0016 19:04
 */
public class LocalSlidingWindowRateLimiter extends AbstractRateLimiter<SlidingWindowRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public LocalSlidingWindowRateLimiter(String limitKey, SlidingWindowRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);
        this.counters = new TreeMap<>();
    }

    // endregion

    // region 变量

    /**
     * 计数器, k：子窗口的时间，value：子窗口的请求数量
     */
    private final TreeMap<Long, Integer> counters;

    // endregion

    // region 接口实现

    /**
     * 申请凭证
     *
     * @param requestCount 请求数量
     * @return 限流结果
     */
    @Override
    public synchronized RateLimitResult acquire(int requestCount) {

        // 获取当前窗口内请求总数
        long currentTotal = this.getCurrentTotal();

        // 剩余可用凭证数量 = 限流上限 - 当前窗口内请求数量总数 - 请求数量
        long remainsPermits = this.limitArgs.getCapacity() - currentTotal - requestCount;

        // 已超限
        if (remainsPermits < 0) {
            return RateLimitResult.forbid();
        }

        // 未超限：计数器累加
        counters.merge(currentTotal, requestCount, Integer::sum);
        return RateLimitResult.allow(remainsPermits);
    }

    // endregion

    // region 私有方法

    /**
     * 获取当前窗口内请求总数
     *
     * @return 结果
     */
    private long getCurrentTotal() {

        // 当前时间
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        // 子窗口大小
        long subTimeCycle = this.limitArgs.getSubTimeCycle();

        // 子窗口数量
        long cycleCount = this.limitArgs.getCycleCount();

        // 当前子窗口时间
        long currentWindowTime = now / subTimeCycle * subTimeCycle;

        // 开始子窗口时间
        long startWindowTime = currentWindowTime - (cycleCount - 1) * subTimeCycle;

        // 总数
        int count = 0;

        // 遍历计数器
        Iterator<Map.Entry<Long, Integer>> iterator = counters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Integer> entry = iterator.next();

            // 窗口过期：移除
            if (entry.getKey() < startWindowTime) {
                iterator.remove();
            }
            // 窗口有效：累计数量
            else {
                count += entry.getValue();
            }
        }

        return count;
    }

    // endregion
}