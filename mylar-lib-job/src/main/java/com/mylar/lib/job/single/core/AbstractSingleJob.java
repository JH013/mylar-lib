package com.mylar.lib.job.single.core;

import com.mylar.lib.base.enhance.SpringResolver;
import com.mylar.lib.job.single.control.ISingleJobControlAggregation;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 单机任务抽象类
 *
 * @author wangz
 * @date 2023/3/9 0009 1:29
 */
@DisallowConcurrentExecution
public abstract class AbstractSingleJob implements Job {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(AbstractSingleJob.class);

    /**
     * 锁
     */
    protected Lock lock;

    // endregion

    // region 接口实现

    /**
     * 任务执行
     *
     * @param jobExecutionContext 任务执行上下文
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        // 任务控制聚合
        ISingleJobControlAggregation jobControlAggregation = SpringResolver.resolve(ISingleJobControlAggregation.class);

        // 当前是否可执行
        boolean enableRunCurrent = jobControlAggregation.enableRunCurrent(jobExecutionContext.getJobDetail().getKey());
        if (!enableRunCurrent) {
            return;
        }

        // 多线程执行
        if (!this.serialExecution(jobExecutionContext)) {
            this.doWork(jobExecutionContext);
            return;
        }

        // 串行执行
        SpringResolver.resolve(this.getClass()).executeWithLock(jobExecutionContext);
    }


    // endregion

    // region 供子类重写

    /**
     * 业务处理
     *
     * @param jobExecutionContext 任务执行上下文
     */
    protected abstract void doWork(JobExecutionContext jobExecutionContext);

    /**
     * 串行执行
     *
     * @param jobExecutionContext 任务执行上下文
     * @return 结果
     */
    protected boolean serialExecution(JobExecutionContext jobExecutionContext) {
        return true;
    }

    // endregion

    // region 私有方法

    /**
     * 获取锁
     *
     * @return 锁
     */
    protected Lock getLock() {
        if (this.lock == null) {
            this.lock = new ReentrantLock();
        }

        return this.lock;
    }

    /**
     * 加锁执行
     *
     * @param jobExecutionContext 任务执行上下文
     */
    protected void executeWithLock(JobExecutionContext jobExecutionContext) {
        Lock lock = this.getLock();
        if (lock.tryLock()) {
            try {
                this.doWork(jobExecutionContext);
            } finally {
                lock.unlock();
            }
        }
    }

    // endregion
}
