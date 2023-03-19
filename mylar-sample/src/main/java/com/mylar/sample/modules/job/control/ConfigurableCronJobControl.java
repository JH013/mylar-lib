package com.mylar.sample.modules.job.control;

import com.mylar.lib.base.utils.SystemConfigUtils;
import com.mylar.lib.job.single.annotation.SingleJobBasicConfig;
import com.mylar.lib.job.single.control.AbstractSingleJobControl;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobKey;

import java.util.Map;

/**
 * 任务控制-可配置 cron
 *
 * @author wangz
 * @date 2023/3/12 0012 20:02
 */
public class ConfigurableCronJobControl extends AbstractSingleJobControl {

    /**
     * 获取 cron
     *
     * @param jobKey       任务键
     * @param basicConfig  基础配置
     * @param extendParams 扩展参数
     * @return cron
     */
    public String getCron(JobKey jobKey, SingleJobBasicConfig basicConfig, Map<String, Object> extendParams) {

        // 配置属性键
        String propertyKey = String.format("mylar.job.single.cron.%s", jobKey.toString());

        // 配置属性值
        String propertyValue = SystemConfigUtils.getProperty(propertyKey);
        if (StringUtils.isNotEmpty(propertyValue)) {
            return propertyValue;
        }

        // 默认返回基础配置
        return basicConfig.cron();
    }
}
