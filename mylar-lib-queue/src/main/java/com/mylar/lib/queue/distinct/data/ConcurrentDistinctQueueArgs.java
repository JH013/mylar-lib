package com.mylar.lib.queue.distinct.data;

import java.time.Instant;

/**
 * 队列参数（并发去重队列）
 *
 * @author wangz
 * @date 2023/12/31 0031 21:23
 */
public class ConcurrentDistinctQueueArgs extends SimpleDistinctQueueArgs {

    /**
     * 执行中数据的数量上限
     */
    private int runningDataCapacity = 1000000;

    /**
     * 执行中数据的过期时间（单位：秒）
     */
    private int runningDataExpire = 300;

    /**
     * 执行中数据的缓存键后缀
     */
    private String runningDataKeySuffix;

    // region getter & setter

    public int getRunningDataCapacity() {
        return runningDataCapacity;
    }

    public void setRunningDataCapacity(int runningDataCapacity) {
        this.runningDataCapacity = runningDataCapacity;
    }

    public int getRunningDataExpire() {
        return runningDataExpire;
    }

    public void setRunningDataExpire(int runningDataExpire) {
        this.runningDataExpire = runningDataExpire;
    }

    public String getRunningDataKeySuffix() {
        return String.valueOf(Instant.now().getEpochSecond() / this.runningDataExpire);
    }

    // endregion
}
