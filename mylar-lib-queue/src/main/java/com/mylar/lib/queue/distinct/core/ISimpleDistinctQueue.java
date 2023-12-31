package com.mylar.lib.queue.distinct.core;

import java.util.List;

/**
 * 简单去重队列
 *
 * @author wangz
 * @date 2024/1/1 0001 2:10
 */
public interface ISimpleDistinctQueue extends IDistinctQueue {

    /**
     * 删除
     *
     * @param dataIds 数据ID集合
     * @return 删除数量
     */
    int remove(List<String> dataIds);
}
