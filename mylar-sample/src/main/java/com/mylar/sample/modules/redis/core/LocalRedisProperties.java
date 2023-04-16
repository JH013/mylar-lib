package com.mylar.sample.modules.redis.core;

import com.mylar.lib.redis.config.BaseRedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangz
 * @date 2023/4/10 0010 0:35
 */
@Configuration
@ConfigurationProperties(prefix = "local.redis", ignoreInvalidFields = true)
public class LocalRedisProperties extends BaseRedisProperties {
}
