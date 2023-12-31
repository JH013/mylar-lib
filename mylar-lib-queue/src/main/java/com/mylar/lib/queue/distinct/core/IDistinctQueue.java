package com.mylar.lib.queue.distinct.core;

import com.mylar.lib.queue.distinct.data.EnqueueStatusEnum;

import java.util.Map;

/**
 * 去重队列
 *
 * @author wangz
 * @date 2023/12/27 0027 22:38
 */
public interface IDistinctQueue {

    /**
     * 入队
     *
     * @param dataId      数据ID
     * @param dataContent 数据内容
     * @return 入队状态
     */
    EnqueueStatusEnum enqueue(String dataId, String dataContent);

    /**
     * 出队
     *
     * @param dequeueCount 出队数量
     * @return 出队数据
     */
    Map<String, String> dequeue(int dequeueCount);
}
