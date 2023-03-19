package com.mylar.sample.modules.job;

import com.mylar.lib.job.single.annotation.SingleJobBasicConfig;
import com.mylar.lib.job.single.core.AbstractSingleJob;
import com.mylar.sample.modules.job.control.annotation.ConfigurableCronJobControlConfig;
import com.mylar.sample.modules.job.control.annotation.SiteTypeJobControlConfig;
import com.mylar.sample.modules.job.control.annotation.TimeRangeJobControlConfig;
import com.mylar.sample.modules.job.listener.MySingleJobListener;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 单机任务-简单打印
 *
 * @author wangz
 * @date 2023/3/12 0012 16:14
 */
@SingleJobBasicConfig(jobName = "mylar-print-job", cron = "0/30 * * * * ?", jobListeners = MySingleJobListener.class)
@SiteTypeJobControlConfig(sitesToRun = "mylar-sample")
@TimeRangeJobControlConfig(timeSpansToRun = {"19:50~19:55", "20:00~20:05", "20:10~23:59"})
@ConfigurableCronJobControlConfig
public class PrintSingleJob extends AbstractSingleJob {

    // region 变量 & 常量

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(PrintSingleJob.class);

    // endregion

    // region 重写基类方法

    /**
     * 业务处理
     *
     * @param jobExecutionContext 任务执行上下文
     */
    @Override
    public void doWork(JobExecutionContext jobExecutionContext) {
        String jobSign = jobExecutionContext.getJobDetail().getKey().toString();
        this.log.info("[{}]-[{}]-[{}] -> print. ", jobSign, Thread.currentThread().getName(), LocalDateTime.now());
    }

    // endregion
}