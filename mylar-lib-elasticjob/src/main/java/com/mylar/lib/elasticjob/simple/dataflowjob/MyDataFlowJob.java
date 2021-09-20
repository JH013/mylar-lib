package com.mylar.lib.elasticjob.simple.dataflowjob;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 任务
 *
 * @author wangz
 * @date 2021/9/20 0020 1:14
 */
@Component
public class MyDataFlowJob implements DataflowJob<String> {

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(MyDataFlowJob.class);

    /**
     * 获取数据
     *
     * @param shardingContext 分片上下文
     * @return 数据
     */
    @Override
    public List<String> fetchData(ShardingContext shardingContext) {
        List<String> list = Arrays.asList("1", "2", "3");
        logger.info(String.format("MyDataFlowJob fetch data - data: %s, context: %s",
                String.join(",", list), shardingContext.toString()));
        return list;
    }

    /**
     * 处理数据
     *
     * @param shardingContext 分片上下文
     * @param list            数据
     */
    @Override
    public void processData(ShardingContext shardingContext, List<String> list) {
        logger.info(String.format("MyDataFlowJob process data - data: %s, context: %s",
                String.join(",", list), shardingContext.toString()));
    }
}
