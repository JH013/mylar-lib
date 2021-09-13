package com.mylar.lib.quartz.biz.job;

import com.mylar.lib.quartz.core.LocalConfig;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * 定时任务-商品添加后置任务
 *
 * @author wangz
 * @date 2021/9/9 0009 1:28
 */
public class GoodsAddAfterTask extends QuartzJobBean {

    /**
     * logback
     */
    static Logger logger = LoggerFactory.getLogger(GoodsAddAfterTask.class);

    /**
     * 执行任务
     *
     * @param jobExecutionContext 任务执行上下文
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {

        // 任务名
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();

        // application name
        String node = LocalConfig.envProperty("spring.application.name");
        logger.info("Execute GoodsAddAfterTask[{}], node: {}, date: {}", jobName, node, new Date());
    }
}
