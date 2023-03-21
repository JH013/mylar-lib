package com.mylar.lib.thread.pool.scheduler;

import com.mylar.lib.thread.pool.annotation.ThreadPoolBasicConfig;
import com.mylar.lib.thread.pool.core.BaseThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 线程池调度器
 *
 * @author wangz
 * @date 2023/3/21 0021 21:59
 */
@Component
public class ThreadPoolScheduler implements IThreadPoolScheduler {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(ThreadPoolScheduler.class);

    /**
     * 基础配置缓存
     */
    private final ConcurrentHashMap<String, ThreadPoolBasicConfig> configCache = new ConcurrentHashMap<>();

    /**
     * 实例缓存
     */
    private final ConcurrentHashMap<String, BaseThreadPoolExecutor> executorCache = new ConcurrentHashMap<>();

    // endregion

    // region 公共方法

    /**
     * 添加线程池配置
     *
     * @param basicConfig 配置
     */
    public void addThreadPoolConfig(ThreadPoolBasicConfig basicConfig) {
        this.configCache.put(basicConfig.key(), basicConfig);
    }

    // endregion

    // region 接口实现

    /**
     * 获取并缓存
     *
     * @param key 线程池键
     * @return 线程池执行器
     */
    public BaseThreadPoolExecutor getAndCache(String key) {

        // 查询缓存配置
        ThreadPoolBasicConfig basicConfig = this.configCache.get(key);
        if (basicConfig == null) {
            this.log.error("Create thread pool executor failed, not found config， key: {}", key);
            throw new RuntimeException("Create thread pool executor failed, not found config");
        }

        // 创建并缓存线程池执行器
        return this.executorCache.computeIfAbsent(basicConfig.key(), t -> this.createThreadPoolExecutor(basicConfig));
    }

    // endregion

    // region 私有方法

    /**
     * 创建线程池执行器
     *
     * @param basicConfig 基础配置
     * @return 线程池执行器
     */
    private BaseThreadPoolExecutor createThreadPoolExecutor(ThreadPoolBasicConfig basicConfig) {
        try {

            // 阻塞队列
            BlockingQueue<Runnable> blockingQueue;
            if (basicConfig.blockingQueueCapacity() > 0) {
                blockingQueue = new LinkedBlockingQueue<>(basicConfig.blockingQueueCapacity());
            } else {
                blockingQueue = new SynchronousQueue<>();
            }

            // 拒绝策略
            RejectedExecutionHandler rejectedPolicy = basicConfig.rejectPolicy().newInstance();

            // 创建线程池执行器
            BaseThreadPoolExecutor executor = new BaseThreadPoolExecutor(
                    basicConfig.key(),
                    basicConfig.corePoolSize(),
                    basicConfig.maximumPoolSize(),
                    basicConfig.keepAliveTime(),
                    basicConfig.unit(),
                    blockingQueue,
                    rejectedPolicy
            );

            // 设置描述
            executor.setDescription(basicConfig.description());

            // 返回结果
            return executor;
        } catch (Exception e) {
            this.log.error("Create thread pool executor failed, key: {}", basicConfig.key(), e);
            throw new RuntimeException("Create thread pool executor failed", e);
        }
    }

    // endregion
}
