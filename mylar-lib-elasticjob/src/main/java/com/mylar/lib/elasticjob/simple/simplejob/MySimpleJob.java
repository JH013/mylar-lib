package com.mylar.lib.elasticjob.simple.simplejob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 任务
 *
 * @author wangz
 * @date 2021/9/19 0019 20:30
 */
@Component
public class MySimpleJob implements SimpleJob {

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(MySimpleJob.class);

    /**
     * 任务执行
     *
     * @param shardingContext 分片上下文
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info(String.format("MySimpleJob - context: %s", shardingContext.toString()));
    }
}
