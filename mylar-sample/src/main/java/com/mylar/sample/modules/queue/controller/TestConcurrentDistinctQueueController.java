package com.mylar.sample.modules.queue.controller;

import com.mylar.lib.queue.distinct.data.ConcurrentDistinctQueueArgs;
import com.mylar.lib.queue.distinct.data.EnqueueStatusEnum;
import com.mylar.lib.queue.distinct.plugins.ConcurrentDistinctQueue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

/**
 * 并发去重队列
 *
 * @author wangz
 * @date 2023/12/28 0028 3:31
 */
@RestController
@RequestMapping(value = "/test/queue/concurrent/distinct")
public class TestConcurrentDistinctQueueController {

    /**
     * 入队
     *
     * @return 结果
     */
    @RequestMapping(value = "/enqueue")
    public String enqueue(String dataId, String dataContent) {
        ConcurrentDistinctQueue concurrentDistinctQueue = new ConcurrentDistinctQueue("mylar", new ConcurrentDistinctQueueArgs());
        EnqueueStatusEnum enqueueStatus = concurrentDistinctQueue.enqueue(dataId, dataContent);
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
        ConcurrentDistinctQueue concurrentDistinctQueue = new ConcurrentDistinctQueue("mylar", new ConcurrentDistinctQueueArgs());
        Map<String, String> ret = concurrentDistinctQueue.dequeue(dequeueCount);
        if (ret == null) {
            return "empty";
        }

        return ret.toString();
    }

    /**
     * 删除等待中数据
     *
     * @param dataIdStr 数据ID字符串（逗号分隔）
     * @return 结果
     */
    @RequestMapping(value = "/removeWaiting")
    public String removeWaiting(String dataIdStr) {
        ConcurrentDistinctQueue concurrentDistinctQueue = new ConcurrentDistinctQueue("mylar", new ConcurrentDistinctQueueArgs());
        int count = concurrentDistinctQueue.removeWaiting(Arrays.asList(dataIdStr.split(",").clone()));
        return String.valueOf(count);
    }

    /**
     * 删除执行中数据
     *
     * @param dataIdStr 数据ID字符串（逗号分隔）
     * @return 结果
     */
    @RequestMapping(value = "/removeRunning")
    public String removeRunning(String dataIdStr) {
        ConcurrentDistinctQueue concurrentDistinctQueue = new ConcurrentDistinctQueue("mylar", new ConcurrentDistinctQueueArgs());
        int count = concurrentDistinctQueue.removeRunning(Arrays.asList(dataIdStr.split(",").clone()));
        return String.valueOf(count);
    }
}
