package com.mylar.lib.redis.config;

import com.mylar.lib.redis.core.IRedisKeyPropertiesMapper;
import com.mylar.lib.redis.operations.IRedisAggregateOperations;
import com.mylar.lib.redis.operations.RedisAggregateOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 自动装配
 *
 * @author wangz
 * @date 2023/2/26 0026 3:47
 */
@Configuration
@ConditionalOnBean({IRedisKeyPropertiesMapper.class})
public class RedisAutoConfigure {

    /**
     * Redis Operations Bean
     *
     * @param keyPropertiesMapper 缓存键与配置属性映射器
     * @return bean
     */
    @Bean
    public IRedisAggregateOperations redisOperations(IRedisKeyPropertiesMapper keyPropertiesMapper) {
        return new RedisAggregateOperations(keyPropertiesMapper);
    }
}
