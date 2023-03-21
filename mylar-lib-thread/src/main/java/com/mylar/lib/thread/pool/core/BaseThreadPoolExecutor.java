package com.mylar.lib.thread.pool.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 线程池执行器
 *
 * @author wangz
 * @date 2023/3/21 0021 22:03
 */
public class BaseThreadPoolExecutor extends ThreadPoolExecutor {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param key             线程池键
     * @param corePoolSize    核心线程池大小
     * @param maximumPoolSize 线程池最大容量
     * @param keepAliveTime   空闲线程的存活时间
     * @param unit            空闲线程的存活时间单位
     * @param workQueue       阻塞队列
     * @param handler         拒绝策略
     */
    public BaseThreadPoolExecutor(
            String key,
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            RejectedExecutionHandler handler
    ) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                new ThreadFactoryBuilder().setNameFormat(String.format("%s-%%d", key)).build(),
                handler
        );

        this.key = key;
    }

    // endregion

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(BaseThreadPoolExecutor.class);

    /**
     * 线程池键
     */
    protected String key;

    /**
     * 线程池描述
     */
    protected String description;

    // endregion

    // region 公共方法

    /**
     * 设置线程池描述
     *
     * @param description 线程池描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    // endregion

    // region 重写基类方法

    /**
     * 执行前
     *
     * @param t Thread
     * @param r Runnable
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    /**
     * 执行后
     *
     * @param t Thread
     * @param r Runnable
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

    // endregion
}
