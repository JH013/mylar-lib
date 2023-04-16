package com.mylar.lib.limiter.plugins.local.blocking;

import com.mylar.lib.limiter.core.AbstractRateLimiter;
import com.mylar.lib.limiter.data.RateLimitResult;
import com.mylar.lib.limiter.data.args.LeakyBucketRateLimitArgs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 本地-漏桶限流-阻塞模式
 *
 * @author wangz
 * @date 2023/4/16 0016 21:58
 */
public class LocalBlockingLeakyBucketRateLimiter extends AbstractRateLimiter<LeakyBucketRateLimitArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param limitKey  限流键
     * @param limitArgs 限流参数
     */
    public LocalBlockingLeakyBucketRateLimiter(String limitKey, LeakyBucketRateLimitArgs limitArgs) {
        super(limitKey, limitArgs);
        this.queue = new LinkedBlockingQueue<>((int) limitArgs.getBurstCapacity());
        this.millsLeakOne = 1000 / limitArgs.getLeakRate();
    }

    // endregion

    // region 变量

    /**
     * 漏桶容器
     */
    private final BlockingQueue<Thread> queue;

    /**
     * 多少毫秒漏出一个
     */
    private final long millsLeakOne;

    // endregion

    // region 接口实现

    /**
     * 申请凭证
     *
     * @param requestCount 请求数量
     * @return 限流结果
     */
    @Override
    public RateLimitResult acquire(int requestCount) {

        // 本地阻塞漏桶限流不支持请求数量大于 1
        if (requestCount > 1) {
            throw new RuntimeException("Local blocking leak bucket rate limiter is not support request count greater than 1.");
        }

        // 是否成功添加到队列
        boolean success;

        // 剩余可用凭证数量
        long remainPermits = 0;

        // 同步入队
        synchronized (this) {
            success = this.queue.offer(Thread.currentThread());
            remainPermits = this.queue.remainingCapacity();
        }

        // 入队成功：阻塞线程并返回成功
        if (success) {
            LockSupport.park();
            return RateLimitResult.allow(remainPermits);
        }

        // 限流
        return RateLimitResult.forbid();
    }

    // endregion

    // region 私有方法

    /**
     * 定时取出请求
     */
    private void work() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {

                // 从队列中取出请求
                Thread thread = this.queue.take();

                // 唤醒阻塞的线程
                LockSupport.unpark(thread);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }, 0, this.millsLeakOne, TimeUnit.MILLISECONDS);
    }

    // endregion
}
