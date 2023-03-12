package com.mylar.lib.base.utils;

import com.mylar.lib.base.config.LocalConfigFacade;
import com.mylar.lib.base.constant.Constants;
import com.mylar.lib.base.enhance.SpringContextHolder;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * 系统配置工具类
 *
 * @author wangz
 * @date 2023/3/12 0012 19:38
 */
public class SystemConfigUtils {

    /**
     * 构造方法
     */
    private SystemConfigUtils() {

    }

    /**
     * 获取配置属性
     *
     * @param key 配置键
     * @return 配置值
     */
    public static String getProperty(String key) {

        // environment
        Environment environment = SpringContextHolder.getContext().getEnvironment();
        return environment.getProperty(key);
    }

    /**
     * 是否支持本站运行
     *
     * @param sitesToRun 可运行站点集合
     * @return 结果
     */
    public static boolean runOnSite(String[] sitesToRun) {

        // 校验入参
        if (sitesToRun == null || sitesToRun.length <= 0) {
            return false;
        }

        // 判断全部站点支持
        List<String> sites = Arrays.asList(sitesToRun);
        if (sites.contains(Constants.ALL)) {
            return true;
        }

        // 判断是否包含本站代码
        String serviceCode = LocalConfigFacade.getInstance().getSystemAppConfig().getSiteType();
        return sites.contains(serviceCode);
    }
}
