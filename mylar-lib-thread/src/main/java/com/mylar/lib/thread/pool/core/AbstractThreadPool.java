package com.mylar.lib.thread.pool.core;

import com.mylar.lib.base.enhance.SpringResolver;
import com.mylar.lib.thread.pool.scheduler.IThreadPoolScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程池抽象基类
 *
 * @author wangz
 * @date 2023/3/21 0021 21:45
 */
public abstract class AbstractThreadPool implements IThreadPool {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(AbstractThreadPool.class);

    /**
     * 线程池键
     */
    protected String key;

    // endregion

    // region 公共方法

    /**
     * 设置线程池键
     *
     * @param key 线程池键
     */
    public void setKey(String key) {
        this.key = key;
    }

    // endregion

    // region 接口实现

    /**
     * 任务执行
     *
     * @param task 任务
     */
    @Override
    public void execute(Runnable task) {
        this.getExecutor().execute(task);
    }

    /**
     * 任务提交
     *
     * @param task 任务
     * @return 凭证
     */
    @Override
    public Future<?> submit(Runnable task) {
        return this.getExecutor().submit(task);
    }

    /**
     * 任务提交
     *
     * @param task 任务
     * @param <T>  泛型
     * @return 凭证
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return this.getExecutor().submit(task);
    }

    /**
     * 执行并等待全部完成
     *
     * @param tasks 任务集合
     */
    @Override
    public void execAndWaitAll(Runnable... tasks) {

        // 凭证集合
        List<Future<?>> futures = new ArrayList<>();

        // 获取线程池执行器
        BaseThreadPoolExecutor executor = this.getExecutor();

        // 循环提交任务
        for (Runnable task : tasks) {
            Future<?> submit = executor.submit(task);
            futures.add(submit);
        }

        // 等待所有任务的结果
        futures.forEach(future -> {

            // 等待结果
            try {
                future.get();
            }
            // 中断异常
            catch (InterruptedException interruptedException) {
                this.log.error("Thread pool task {} is interrupted.", this.key, interruptedException);
                Thread.currentThread().interrupt();
            }
            // 其他异常
            catch (Exception e) {
                this.log.error("Thread pool task {} execute failed.", this.key, e);
            }
            // 取消掉因异常或其他原因而导致未完成的任务
            finally {
                if (!future.isDone()) {
                    future.cancel(true);
                }
            }
        });
    }

    // endregion

    // region 私有方法

    /**
     * 获取线程池执行器
     *
     * @return 线程池执行器
     */
    private BaseThreadPoolExecutor getExecutor() {

        // 获取调度器
        IThreadPoolScheduler scheduler = SpringResolver.resolve(IThreadPoolScheduler.class);

        // 查询并缓存线程池执行器
        return scheduler.getAndCache(this.key);
    }

    // endregion
}
