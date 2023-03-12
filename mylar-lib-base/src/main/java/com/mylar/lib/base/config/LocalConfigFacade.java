package com.mylar.lib.base.config;

import com.mylar.lib.base.enhance.SpringResolver;

/**
 * 本地配置门面
 *
 * @author wangz
 * @date 2023/3/12 0012 15:48
 */
public enum LocalConfigFacade {

    /**
     * 实例
     */
    INSTANCE;

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static LocalConfigFacade getInstance() {
        return INSTANCE;
    }

    /**
     * 获取系统应用配置
     *
     * @return 系统应用配置
     */
    public SystemAppConfig getSystemAppConfig() {
        return SpringResolver.resolve(SystemAppConfig.class);
    }
}
