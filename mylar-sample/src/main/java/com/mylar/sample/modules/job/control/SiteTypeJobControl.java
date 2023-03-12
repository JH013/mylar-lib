package com.mylar.sample.modules.job.control;

import com.mylar.lib.base.utils.SystemConfigUtils;
import com.mylar.lib.job.annotation.SingleJobBasicConfig;
import com.mylar.lib.job.control.AbstractSingleJobControl;
import org.quartz.JobKey;

import java.util.Map;

/**
 * 任务控制-运行站点
 *
 * @author wangz
 * @date 2023/3/12 0012 15:53
 */
public class SiteTypeJobControl extends AbstractSingleJobControl {

    /**
     * 是否初始化
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param extendParams 扩展参数
     * @return 是否初始化
     */
    public boolean enableInit(JobKey jobKey, SingleJobBasicConfig basicConfig, Map<String, Object> extendParams) {
        Object sitesToRun = extendParams.get("sitesToRun");
        if (sitesToRun == null) {
            return false;
        }

        return SystemConfigUtils.runOnSite((String[]) sitesToRun);
    }
}