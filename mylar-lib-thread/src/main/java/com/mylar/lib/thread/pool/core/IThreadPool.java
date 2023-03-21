package com.mylar.lib.thread.pool.core;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程池接口
 *
 * @author wangz
 * @date 2023/3/21 0021 21:47
 */
public interface IThreadPool {

    /**
     * 任务执行
     *
     * @param task 任务
     */
    void execute(Runnable task);

    /**
     * 任务提交
     *
     * @param task 任务
     * @return 凭证
     */
    Future<?> submit(Runnable task);

    /**
     * 任务提交
     *
     * @param task 任务
     * @param <T>  泛型
     * @return 凭证
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * 执行并等待全部完成
     *
     * @param tasks 任务集合
     */
    void execAndWaitAll(Runnable... tasks);
}
