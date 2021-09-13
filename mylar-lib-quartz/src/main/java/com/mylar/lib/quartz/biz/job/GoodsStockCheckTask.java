package com.mylar.lib.quartz.biz.job;

import com.mylar.lib.quartz.core.LocalConfig;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * 定时任务-商品库存检查任务
 *
 * @author wangz
 * @date 2021/9/9 0009 1:30
 */
public class GoodsStockCheckTask extends QuartzJobBean {

    /**
     * logback
     */
    static Logger logger = LoggerFactory.getLogger(GoodsStockCheckTask.class);

    /**
     * 执行任务
     *
     * @param jobExecutionContext 任务执行上下文
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {

        // 任务名
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();

        // 获取任务详情内的数据集合
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        // 获取商品Id
        Long goodsId = dataMap.getLong("goodsId");

        // application name
        String node = LocalConfig.envProperty("spring.application.name");

        logger.info("Execute GoodsStockCheckTask[{}], node: {}, goods id: {}, date: {}",
                jobName, node, goodsId, new Date());
    }
}
