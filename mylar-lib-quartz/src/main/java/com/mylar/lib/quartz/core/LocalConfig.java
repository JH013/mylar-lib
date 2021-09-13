package com.mylar.lib.quartz.core;

import org.springframework.core.env.Environment;

/**
 * 配置
 *
 * @author wangz
 * @date 2021/9/12 0012 22:19
 */
public class LocalConfig {

    public static Environment environment;

    public static String envProperty(String key) {
        return environment.getProperty(key);
    }
}
