package com.mylar.sample.modules.job.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义单机任务监听器
 *
 * @author wangz
 * @date 2023/3/9 0009 22:55
 */
public class MySingleJobListener extends JobListenerSupport {

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(MySingleJobListener.class);

    /**
     * 获取名称
     *
     * @return 名称
     */
    @Override
    public String getName() {
        return "my-single-job-listener";
    }

    /**
     * 任务是否执行完成
     *
     * @param context      任务执行上下文
     * @param jobException 异常
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        this.log.info("[{}] job {} was executed.", this.getName(), context.getJobDetail().getKey().toString());
    }
}
