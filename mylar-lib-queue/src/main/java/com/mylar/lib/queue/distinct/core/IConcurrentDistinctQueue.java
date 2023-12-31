package com.mylar.lib.queue.distinct.core;

import java.util.List;

/**
 * 并发去重队列
 *
 * @author wangz
 * @date 2024/1/1 0001 2:10
 */
public interface IConcurrentDistinctQueue extends IDistinctQueue {

    /**
     * 删除等待中数据
     *
     * @param dataIds 数据ID集合
     * @return 删除数量
     */
    int removeWaiting(List<String> dataIds);

    /**
     * 删除执行中数据
     *
     * @param dataIds 数据ID集合
     * @return 删除数量
     */
    int removeRunning(List<String> dataIds);
}
