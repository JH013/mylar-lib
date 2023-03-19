package com.mylar.lib.job.single.control;

import com.mylar.lib.job.single.annotation.SingleJobBasicConfig;
import org.quartz.JobKey;

import java.util.Map;

/**
 * 单机任务控制抽象类
 *
 * @author wangz
 * @date 2023/3/12 0012 15:55
 */
public abstract class AbstractSingleJobControl implements ISingleJobControl {

    /**
     * 是否初始化
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param extendParams 扩展参数
     * @return 是否初始化
     */
    public boolean enableInit(JobKey jobKey, SingleJobBasicConfig basicConfig, Map<String, Object> extendParams) {
        return true;
    }

    /**
     * 获取 cron
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param extendParams 扩展参数
     * @return cron
     */
    public String getCron(JobKey jobKey, SingleJobBasicConfig basicConfig, Map<String, Object> extendParams) {
        return null;
    }

    /**
     * 当前是否可执行
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param extendParams 扩展参数
     * @return 是否可执行
     */
    public boolean enableRunCurrent(JobKey jobKey, SingleJobBasicConfig basicConfig, Map<String, Object> extendParams) {
        return true;
    }
}
