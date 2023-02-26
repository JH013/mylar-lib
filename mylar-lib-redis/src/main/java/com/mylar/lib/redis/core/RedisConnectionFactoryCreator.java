package com.mylar.lib.redis.core;

import com.mylar.lib.redis.config.BaseRedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Arrays;

/**
 * Redis 连接工厂构建器
 *
 * @author wangz
 * @date 2023/2/25 0025 10:56
 */
public class RedisConnectionFactoryCreator {

    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(RedisConnectionFactoryCreator.class);

    // region 公共方法

    /**
     * 创建连接工厂
     *
     * @param properties 配置属性
     * @return 连接工厂
     */
    public RedisConnectionFactory create(BaseRedisProperties properties) {

        // 创建集群连接工厂
        if (properties.redisCluster()) {
            return this.createClusterFactory(properties);
        }

        // 创建单点连接工厂
        return this.createStandaloneFactory(properties);
    }

    // endregion

    // region 私有方法

    /**
     * 创建单点连接工厂
     *
     * @param properties 配置属性
     * @return 连接工厂
     */
    private RedisConnectionFactory createStandaloneFactory(BaseRedisProperties properties) {

        // 创建连接池配置
        JedisPoolConfig jedisPoolConfig = this.createPoolConfig(properties);

        // 单点配置
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(properties.getHost());
        redisStandaloneConfiguration.setPort(properties.getPort());
        redisStandaloneConfiguration.setPassword(properties.getPassword());
        redisStandaloneConfiguration.setDatabase(properties.getDatabase());

        // 设置连接超时时间
        JedisClientConfiguration.JedisClientConfigurationBuilder configurationBuilder = JedisClientConfiguration.builder();
        configurationBuilder.connectTimeout(Duration.ofMillis(properties.getTimeout()));

        // 是否使用ssl
        if (properties.getSsl()) {
            configurationBuilder.useSsl();
        }

        // 创建连接
        JedisClientConfiguration jedisClientConfiguration = configurationBuilder.usePooling().poolConfig(jedisPoolConfig).build();
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);

        connectionFactory.afterPropertiesSet();

        log.info("Redis standalone connection factory created completely, url: {}", properties.getHost());
        return connectionFactory;
    }

    /**
     * 创建集群连接工厂
     *
     * @param properties 配置属性
     * @return 连接工厂
     */
    private RedisConnectionFactory createClusterFactory(BaseRedisProperties properties) {

        // 创建连接池配置
        JedisPoolConfig jedisPoolConfig = this.createPoolConfig(properties);

        // 集群配置
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(Arrays.asList(properties.getNodes().split(",")));
        redisClusterConfiguration.setPassword(properties.getPassword());

        // 设置连接超时时间
        JedisClientConfiguration.JedisClientConfigurationBuilder configurationBuilder = JedisClientConfiguration.builder();
        configurationBuilder.connectTimeout(Duration.ofMillis(properties.getTimeout()));

        // 是否使用ssl
        if (properties.getSsl()) {
            configurationBuilder.useSsl();
        }

        // 创建连接
        JedisClientConfiguration jedisClientConfiguration = configurationBuilder.usePooling().poolConfig(jedisPoolConfig).build();
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisClusterConfiguration, jedisClientConfiguration);

        log.info("Redis cluster connection factory created completely, url: {}", properties.getHost());
        return connectionFactory;
    }

    /**
     * 创建连接池配置
     *
     * @param properties 配置属性
     * @return 结果
     */
    private JedisPoolConfig createPoolConfig(BaseRedisProperties properties) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(properties.getMaxTotal());
        jedisPoolConfig.setMaxIdle(properties.getMaxIdle());
        jedisPoolConfig.setMinIdle(properties.getMinIdle());
        return jedisPoolConfig;
    }

    // endregion
}
