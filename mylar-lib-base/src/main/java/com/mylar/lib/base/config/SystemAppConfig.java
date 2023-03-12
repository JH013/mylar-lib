package com.mylar.lib.base.config;

import com.mylar.lib.base.enhance.SpringResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 系统应用配置
 *
 * @author wangz
 * @date 2023/3/12 0012 15:31
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "system")
public class SystemAppConfig {

    /**
     * 站点类型
     */
    private String siteType;

    /**
     * 获取系统应用配置
     *
     * @return 系统应用配置
     */
    public static SystemAppConfig get() {
        return SpringResolver.resolve(SystemAppConfig.class);
    }

    // region getter & setter

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    // endregion
}
