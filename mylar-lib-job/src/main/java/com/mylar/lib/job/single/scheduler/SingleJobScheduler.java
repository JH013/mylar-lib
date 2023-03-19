package com.mylar.lib.job.single.scheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 单机任务调度器
 *
 * @author wangz
 * @date 2023/3/9 0009 23:26
 */
@Component
public class SingleJobScheduler implements ISingleJobScheduler {

    // region 构造方法

    /**
     * 构造方法
     */
    public SingleJobScheduler() {
        try {

            // 获取默认任务调度器
            this.scheduler = StdSchedulerFactory.getDefaultScheduler();

            // 启动任务调度器
            this.startScheduler();
        } catch (Exception e) {
            this.log.error("start single job scheduler failed.", e);
        }
    }

    /**
     * 构造方法
     *
     * @param scheduler 任务调度器
     */
    public SingleJobScheduler(Scheduler scheduler) {
        try {
            this.scheduler = scheduler;

            // 启动任务调度器
            this.startScheduler();
        } catch (Exception e) {
            this.log.error("start single job scheduler failed.", e);
        }
    }

    // endregion

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(SingleJobScheduler.class);

    /**
     * 任务调度器
     */
    private Scheduler scheduler;

    // endregion

    // region 接口实现

    /**
     * 获取任务调度器
     *
     * @return 任务调度器
     */
    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    // endregion

    // region 私有方法

    /**
     * 启动任务调度器
     *
     * @throws SchedulerException 异常
     */
    private void startScheduler() throws SchedulerException {

        // 校验是否为空
        if (this.scheduler == null) {
            log.error("Start single job scheduler failed, scheduler is null.");
            return;
        }

        // 添加关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    scheduler.shutdown();
                } catch (SchedulerException e) {
                    log.error("Close single job scheduler failed.", e);
                }
            }
        });

        // 启动调度器
        this.scheduler.start();
    }

    // endregion
}
