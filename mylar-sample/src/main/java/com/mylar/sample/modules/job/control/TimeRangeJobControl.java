package com.mylar.sample.modules.job.control;

import com.mylar.lib.base.utils.TimeSpanUtils;
import com.mylar.lib.job.single.annotation.SingleJobBasicConfig;
import com.mylar.lib.job.single.control.AbstractSingleJobControl;
import org.quartz.JobKey;

import java.util.Map;

/**
 * 任务控制-运行时间区间
 *
 * @author wangz
 * @date 2023/3/12 0012 18:18
 */
public class TimeRangeJobControl extends AbstractSingleJobControl {

    /**
     * 当前是否可执行
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param extendParams 扩展参数
     * @return 是否可执行
     */
    public boolean enableRunCurrent(JobKey jobKey, SingleJobBasicConfig basicConfig, Map<String, Object> extendParams) {
        Object timeSpansToRun = extendParams.get("timeSpansToRun");
        if (timeSpansToRun == null) {
            return false;
        }

        return TimeSpanUtils.currentInTimeSpanStr((String[]) timeSpansToRun);
    }
}
