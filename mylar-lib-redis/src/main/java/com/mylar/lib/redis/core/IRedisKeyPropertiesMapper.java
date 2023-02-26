package com.mylar.lib.redis.core;

import com.mylar.lib.redis.config.BaseRedisProperties;

/**
 * Redis 缓存键与配置属性映射器
 *
 * @author wangz
 * @date 2023/2/26 0026 1:43
 */
public interface IRedisKeyPropertiesMapper {

    /**
     * 缓存键映射成缓存类型
     *
     * @param cacheKey 缓存键
     * @return 缓存类型
     */
    String mapToCacheType(String cacheKey);

    /**
     * 缓存类型映射成配置属性
     *
     * @param cacheType 缓存类型
     * @return 配置属性
     */
    BaseRedisProperties mapToProperties(String cacheType);
}
