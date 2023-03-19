package com.mylar.sample.modules.job;

import com.mylar.lib.job.annotation.SingleJobBasicConfig;
import com.mylar.lib.job.core.AbstractSingleJob;
import com.mylar.sample.modules.job.control.annotation.ConfigurableCronJobControlConfig;
import com.mylar.sample.modules.job.control.annotation.SiteTypeJobControlConfig;
import com.mylar.sample.modules.job.listener.MySingleJobListener;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 单机任务-简单示例
 *
 * @author wangz
 * @date 2023/3/9 0009 1:36
 */
@SingleJobBasicConfig(jobName = "mylar-sample-job", cron = "0/10 * * * * ?", jobListeners = MySingleJobListener.class)
@SiteTypeJobControlConfig(sitesToRun = "mylar-sample")
@ConfigurableCronJobControlConfig
public class SampleSingleJob extends AbstractSingleJob {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(PrintSingleJob.class);

    // endregion

    // region 重写基类方法

    @Override
    protected void doWork(JobExecutionContext jobExecutionContext) {
        String jobSign = jobExecutionContext.getJobDetail().getKey().toString();
        this.log.info("[{}]-[{}]-[{}] -> start. ", jobSign, Thread.currentThread().getName(), LocalDateTime.now());

        try {
            Thread.sleep(40000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        this.log.info("[{}]-[{}]-[{}] -> finish. ", jobSign, Thread.currentThread().getName(), LocalDateTime.now());
    }

    /**
     * 串行执行
     *
     * @param jobExecutionContext 任务执行上下文
     * @return 结果
     */
    protected boolean serialExecution(JobExecutionContext jobExecutionContext) {
        return false;
    }

    // endregion
}
