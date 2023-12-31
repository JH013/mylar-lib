package com.mylar.sample.modules.queue.controller;

import com.mylar.lib.queue.distinct.data.EnqueueStatusEnum;
import com.mylar.lib.queue.distinct.data.SimpleDistinctQueueArgs;
import com.mylar.lib.queue.distinct.plugins.SimpleDistinctQueue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * 简单去重队列
 *
 * @author wangz
 * @date 2023/12/28 0028 3:31
 */
@RestController
@RequestMapping(value = "/test/queue/simple/distinct")
public class TestSimpleDistinctQueueController {

    /**
     *入队
     *
     * @return 结果
     */
    @RequestMapping(value = "/enqueue")
    public String enqueue(String dataId, String dataContent) {
        SimpleDistinctQueue simpleDistinctQueue = new SimpleDistinctQueue("mylar", new SimpleDistinctQueueArgs());
        EnqueueStatusEnum enqueueStatus = simpleDistinctQueue.enqueue(dataId, dataContent);
        return enqueueStatus.getName();
    }


    /**
     * 出队
     *
     * @param dequeueCount 出队数量
     * @return 结果
     */
    @RequestMapping(value = "/dequeue")
    public String dequeue(Integer dequeueCount) {
        SimpleDistinctQueue simpleDistinctQueue = new SimpleDistinctQueue("mylar", new SimpleDistinctQueueArgs());
        Map<String, String> ret = simpleDistinctQueue.dequeue(dequeueCount);
        if (ret == null) {
            return "empty";
        }

        return ret.toString();
    }

    /**
     * 删除
     *
     * @param dataIdStr 数据ID字符串（逗号分隔）
     * @return 结果
     */
    @RequestMapping(value = "/remove")
    public String remove(String dataIdStr) {
        SimpleDistinctQueue simpleDistinctQueue = new SimpleDistinctQueue("mylar", new SimpleDistinctQueueArgs());
        int count = simpleDistinctQueue.remove(Arrays.asList(dataIdStr.split(",").clone()));
        return String.valueOf(count);
    }
}
