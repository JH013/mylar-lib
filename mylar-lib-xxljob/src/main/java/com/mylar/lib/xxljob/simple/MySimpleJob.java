package com.mylar.lib.xxljob.simple;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wangz
 * @date 2021/9/20 0020 21:11
 */
@Component
public class MySimpleJob {

    private static final Logger logger = LoggerFactory.getLogger(MySimpleJob.class);

    /**
     * 任务处理 - 简单任务
     */
    @XxlJob("simpleJobHandler")
    public void simpleJobHandler() {
        logger.info("Simple Job Handler - executed");
    }

    /**
     * 任务处理 - 分片任务
     */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() {
        logger.info(String.format("Sharding Job Handler - executed, sharding index: %s, sharding total: %s, params: %s",
                XxlJobHelper.getShardIndex(), XxlJobHelper.getShardTotal(), XxlJobHelper.getJobParam()));
    }
}
