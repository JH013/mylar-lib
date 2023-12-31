package com.mylar.lib.queue.distinct.data;

import com.mylar.lib.queue.distinct.core.IDistinctQueueArgs;

/**
 * 队列参数（简单去重队列）
 *
 * @author wangz
 * @date 2023/12/28 0028 2:29
 */
public class SimpleDistinctQueueArgs implements IDistinctQueueArgs {

    /**
     * 等待中数据的数量上限
     */
    private int waitingDataCapacity = 1000000;

    /**
     * 等待中数据的过期时间（单位：秒）
     */
    private int waitingDataExpire = 86400;

    /**
     * 当等待中已存在时更新数据内容
     */
    private boolean updateWhenWaitingExist = false;

    // region getter & setter

    public int getWaitingDataCapacity() {
        return waitingDataCapacity;
    }

    public void setWaitingDataCapacity(int waitingDataCapacity) {
        this.waitingDataCapacity = waitingDataCapacity;
    }

    public int getWaitingDataExpire() {
        return waitingDataExpire;
    }

    public void setWaitingDataExpire(int waitingDataExpire) {
        this.waitingDataExpire = waitingDataExpire;
    }

    public boolean isUpdateWhenWaitingExist() {
        return updateWhenWaitingExist;
    }

    public void setUpdateWhenWaitingExist(boolean updateWhenWaitingExist) {
        this.updateWhenWaitingExist = updateWhenWaitingExist;
    }

    // endregion
}
