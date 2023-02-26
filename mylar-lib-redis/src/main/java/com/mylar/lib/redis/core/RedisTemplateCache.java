package com.mylar.lib.redis.core;

import com.mylar.lib.redis.config.BaseRedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * RedisTemplate 缓存
 *
 * @author wangz
 * @date 2023/2/26 0026 2:39
 */
public class RedisTemplateCache {

    /**
     * 构造方法
     *
     * @param keyPropertiesMapper Redis 缓存键与配置属性映射器
     */
    public RedisTemplateCache(IRedisKeyPropertiesMapper keyPropertiesMapper) {
        this.redisTemplateMap = new HashMap<>();
        this.keyPropertiesMapper = keyPropertiesMapper;
    }

    // region 变量

    /**
     * RedisTemplate 字典
     */
    private final Map<String, StringRedisTemplate> redisTemplateMap;

    /**
     * Redis 缓存键与配置属性映射器
     */
    private final IRedisKeyPropertiesMapper keyPropertiesMapper;

    // endregion

    // region 公共方法

    /**
     * 获取 RedisTemplate
     *
     * @param cacheKey 缓存键
     * @return RedisTemplate
     */
    public StringRedisTemplate getRedisTemplate(String cacheKey) {

        // 缓存键映射成缓存类型
        String cacheType = this.keyPropertiesMapper.mapToCacheType(cacheKey);

        // 根据缓存类型查询字典
        StringRedisTemplate redisTemplate = this.redisTemplateMap.get(cacheType);
        if (redisTemplate == null) {

            // 创建 RedisTemplate 并同步到缓存字典
            redisTemplate = this.createRedisTemplateAndSync(cacheType);
        }

        return redisTemplate;
    }

    // endregion

    // region 私有方法

    /**
     * 创建 RedisTemplate 并同步到缓存字典
     *
     * @param cacheType 缓存类型
     * @return RedisTemplate
     */
    private StringRedisTemplate createRedisTemplateAndSync(String cacheType) {
        synchronized (this) {
            return this.redisTemplateMap.computeIfAbsent(cacheType, key -> {

                // 缓存类型映射成配置属性
                BaseRedisProperties properties = this.keyPropertiesMapper.mapToProperties(cacheType);

                // 创建连接工厂
                RedisConnectionFactory connectionFactory = new RedisConnectionFactoryCreator().create(properties);

                // 创建 RedisTemplate
                return new StringRedisTemplate(connectionFactory);
            });
        }
    }

    // endregion
}
