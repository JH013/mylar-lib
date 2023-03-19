package com.mylar.lib.job.distribute.core;

/**
 * 简易分布式定时任务数据
 *
 * @author wangz
 * @date 2023/3/19 0019 21:15
 */
public interface SimpleDistributeJobData {

    /**
     * 获取唯一标识
     *
     * @return 唯一标识
     */
    String uniqueSign();
}
