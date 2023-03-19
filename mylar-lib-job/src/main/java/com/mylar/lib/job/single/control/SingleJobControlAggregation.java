package com.mylar.lib.job.single.control;

import com.mylar.lib.job.single.annotation.SingleJobBasicConfig;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 单机任务控制聚合
 *
 * @author wangz
 * @date 2023/3/12 0012 5:39
 */
@Component
public class SingleJobControlAggregation implements ISingleJobControlAggregation {

    // region 构造方法

    /**
     * 构造方法
     */
    public SingleJobControlAggregation() {
        this.basicConfigMap = new HashMap<>();
        this.controlParamMap = new HashMap<>();
    }

    // endregion

    // region 变量 & 常量

    /**
     * 任务基础配置字典
     */
    private final Map<JobKey, SingleJobBasicConfig> basicConfigMap;

    /**
     * 任务控制参数字典
     */
    private final Map<JobKey, SingleJobControlParam> controlParamMap;

    // endregion

    // region 接口实现

    /**
     * 是否初始化
     *
     * @param jobKey 任务键
     * @return 是否初始化
     */
    @Override
    public boolean enableInit(JobKey jobKey) {

        // 任务基础配置
        SingleJobBasicConfig basicConfig = basicConfigMap.get(jobKey);
        if (basicConfig == null) {
            return false;
        }

        // 任务控制参数
        SingleJobControlParam controlParam = controlParamMap.get(jobKey);
        if (controlParam == null || CollectionUtils.isEmpty(controlParam.getItems())) {

            // 默认执行初始化
            return true;
        }

        // 判断是否初始化
        for (SingleJobControlParam.Item item : controlParam.getItems()) {
            if (!item.getControl().enableInit(jobKey, basicConfig, item.getParams())) {
                return false;
            }
        }

        // 默认执行初始化
        return true;
    }

    /**
     * 获取 cron
     *
     * @param jobKey 任务键
     * @return cron
     */
    @Override
    public String getCron(JobKey jobKey) {

        // 任务基础配置
        SingleJobBasicConfig basicConfig = basicConfigMap.get(jobKey);
        if (basicConfig == null) {
            return null;
        }

        // 优先从任务控制参数中获取 cron
        SingleJobControlParam controlParam = controlParamMap.get(jobKey);
        if (controlParam != null && !CollectionUtils.isEmpty(controlParam.getItems())) {
            for (SingleJobControlParam.Item item : controlParam.getItems()) {
                String cron = item.getControl().getCron(jobKey, basicConfig, item.getParams());
                if (StringUtils.isNotEmpty(cron)) {
                    return cron;
                }
            }
        }

        // 默认从任务基础配置中获取 cron
        return basicConfig.cron();
    }

    /**
     * 当前是否可执行
     *
     * @param jobKey 任务键
     * @return 是否可执行
     */
    @Override
    public boolean enableRunCurrent(JobKey jobKey) {

        // 任务基础配置
        SingleJobBasicConfig basicConfig = basicConfigMap.get(jobKey);
        if (basicConfig == null) {
            return false;
        }

        // 任务控制参数
        SingleJobControlParam controlParam = controlParamMap.get(jobKey);
        if (controlParam == null || CollectionUtils.isEmpty(controlParam.getItems())) {

            // 默认可执行
            return true;
        }

        // 判断是否可执行
        for (SingleJobControlParam.Item item : controlParam.getItems()) {
            if (!item.getControl().enableRunCurrent(jobKey, basicConfig, item.getParams())) {
                return false;
            }
        }

        // 默认可执行
        return true;
    }

    // endregion

    // region 公共方法

    /**
     * 添加控制
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param controlParam 控制参数
     */
    public void addControl(JobKey jobKey, SingleJobBasicConfig basicConfig, SingleJobControlParam controlParam) {
        this.basicConfigMap.put(jobKey, basicConfig);
        this.controlParamMap.put(jobKey, controlParam);
    }

    // endregion
}
