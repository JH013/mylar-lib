package com.mylar.lib.thread.pool.scheduler;

import com.mylar.lib.thread.pool.core.BaseThreadPoolExecutor;

/**
 * 线程池调度器
 *
 * @author wangz
 * @date 2023/3/21 0021 21:58
 */
public interface IThreadPoolScheduler {

    /**
     * 获取并缓存
     *
     * @param key 线程池键
     * @return 线程池执行器
     */
    BaseThreadPoolExecutor getAndCache(String key);
}
