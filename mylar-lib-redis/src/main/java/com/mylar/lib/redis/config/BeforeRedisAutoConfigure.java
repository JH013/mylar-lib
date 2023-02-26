package com.mylar.lib.redis.config;

import com.mylar.lib.redis.core.IRedisKeyPropertiesMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangz
 * @date 2023/2/26 0026 14:55
 */
//@Configuration
//@EnableConfigurationProperties(BaseRedisProperties.class)
//public class BeforeRedisAutoConfigure {
//
//    @Bean
//    @ConditionalOnMissingBean
//    public IRedisKeyPropertiesMapper cacheKeyToCacheTypeConverter() {
//        return new DefaultCacheKeyToCacheTypeConverter();
//    }
//}
