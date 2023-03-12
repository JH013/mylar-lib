package com.mylar.sample.modules.redis.core;

import com.mylar.lib.redis.config.BaseRedisProperties;
import com.mylar.lib.redis.core.IRedisKeyPropertiesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义缓存键与配置属性映射器
 *
 * @author wangz
 * @date 2023/2/26 0026 14:33
 */
@Component
public class MyRedisKeyPropertiesMapper implements IRedisKeyPropertiesMapper {

    /**
     * 自定义配置属性
     */
    @Autowired
    private MyRedisProperties myRedisProperties;

    /**
     * 缓存键映射成缓存类型
     *
     * @param cacheKey 缓存键
     * @return 缓存类型
     */
    @Override
    public String mapToCacheType(String cacheKey) {
        return "BASE";
    }

    /**
     * 缓存类型映射成配置属性
     *
     * @param cacheType 缓存类型
     * @return 配置属性
     */
    @Override
    public BaseRedisProperties mapToProperties(String cacheType) {
        return this.myRedisProperties;
    }
}
