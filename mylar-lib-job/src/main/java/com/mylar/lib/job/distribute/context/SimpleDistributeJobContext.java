package com.mylar.lib.job.distribute.context;

import java.util.List;

/**
 * 简易分布式定时任务上下文
 *
 * @author wangz
 * @date 2023/3/19 0019 21:19
 */
public class SimpleDistributeJobContext {

    /**
     * 所有实例
     */
    private List<String> allInstance;

    /**
     * 当前实例
     */
    private String currentInstance;

    // region getter & setter

    public List<String> getAllInstance() {
        return allInstance;
    }

    public void setAllInstance(List<String> allInstance) {
        this.allInstance = allInstance;
    }

    public String getCurrentInstance() {
        return currentInstance;
    }

    public void setCurrentInstance(String currentInstance) {
        this.currentInstance = currentInstance;
    }

    // endregion
}
