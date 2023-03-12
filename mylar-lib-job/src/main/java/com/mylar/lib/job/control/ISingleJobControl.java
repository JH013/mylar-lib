package com.mylar.lib.job.control;

import com.mylar.lib.job.annotation.SingleJobBasicConfig;
import org.quartz.JobKey;

import java.util.Map;

/**
 * 单机任务控制
 *
 * @author wangz
 * @date 2023/3/11 0011 17:36
 */
public interface ISingleJobControl {

    /**
     * 是否初始化
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param extendParams 扩展参数
     * @return 是否初始化
     */
    boolean enableInit(JobKey jobKey, SingleJobBasicConfig basicConfig, Map<String, Object> extendParams);

    /**
     * 获取 cron
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param extendParams 扩展参数
     * @return cron
     */
    String getCron(JobKey jobKey, SingleJobBasicConfig basicConfig, Map<String, Object> extendParams);

    /**
     * 当前是否可执行
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param extendParams 扩展参数
     * @return 是否可执行
     */
    boolean enableRunCurrent(JobKey jobKey, SingleJobBasicConfig basicConfig, Map<String, Object> extendParams);
}
