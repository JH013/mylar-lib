package com.mylar.sample.modules.redis.core;

import com.mylar.lib.redis.config.BaseRedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义配置属性
 *
 * @author wangz
 * @date 2023/2/26 0026 14:35
 */
@Configuration
@ConfigurationProperties(prefix = "my.redis", ignoreInvalidFields = true)
public class MyRedisProperties extends BaseRedisProperties {
}
