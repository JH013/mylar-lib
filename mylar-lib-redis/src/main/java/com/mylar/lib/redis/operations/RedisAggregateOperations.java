package com.mylar.lib.redis.operations;

import com.mylar.lib.redis.core.IRedisKeyPropertiesMapper;
import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.operations.sub.*;
import com.mylar.lib.redis.operations.sub.impl.*;

/**
 * Redis Operations 聚合
 *
 * @author wangz
 * @date 2023/2/26 0026 4:19
 */
public class RedisAggregateOperations implements IRedisAggregateOperations {

    /**
     * 构造方法
     *
     * @param keyPropertiesMapper 缓存键与配置属性映射器
     */
    public RedisAggregateOperations(IRedisKeyPropertiesMapper keyPropertiesMapper) {

        // RedisTemplate 缓存
        RedisTemplateCache redisTemplateCache = new RedisTemplateCache(keyPropertiesMapper);

        // 初始化 Redis Operations
        this.keyOperations = new RedisKeySubOperations(redisTemplateCache);
        this.stringOperations = new RedisStringSubOperations(redisTemplateCache);
        this.hashOperations = new RedisHashSubOperations(redisTemplateCache);
        this.listOperations = new RedisListSubOperations(redisTemplateCache);
        this.setOperations = new RedisSetSubOperations(redisTemplateCache);
        this.sortSetOperations = new RedisSortSetSubOperations(redisTemplateCache);
        this.scriptOperations = new RedisScriptSubOperations(redisTemplateCache);
    }

    // region 变量

    /**
     * Redis Sub Operations - Key
     */
    private final IRedisKeySubOperations keyOperations;

    /**
     * Redis Sub Operations - String
     */
    private final IRedisStringSubOperations stringOperations;

    /**
     * Redis Sub Operations - Hash
     */
    private final IRedisHashSubOperations hashOperations;

    /**
     * Redis Sub Operations - List
     */
    private final IRedisListSubOperations listOperations;

    /**
     * Redis Sub Operations - Set
     */
    private final IRedisSetSubOperations setOperations;

    /**
     * Redis Sub Operations - Sort Set
     */
    private final IRedisSortSetSubOperations sortSetOperations;

    /**
     * Redis Sub Operations - Lua Script
     */
    private final IRedisScriptSubOperations scriptOperations;

    // endregion

    // region 公共方法

    /**
     * 获取 Redis Sub Operations - Key
     *
     * @return 结果
     */
    @Override
    public IRedisKeySubOperations opsKey() {
        return this.keyOperations;
    }

    /**
     * 获取 Redis Sub Operations - String
     *
     * @return 结果
     */
    @Override
    public IRedisStringSubOperations opsString() {
        return this.stringOperations;
    }

    /**
     * 获取 Redis Sub Operations - Hash
     *
     * @return 结果
     */
    @Override
    public IRedisHashSubOperations opsHash() {
        return this.hashOperations;
    }

    /**
     * 获取 Redis Sub Operations - List
     *
     * @return 结果
     */
    @Override
    public IRedisListSubOperations opsList() {
        return this.listOperations;
    }

    /**
     * 获取 Redis Sub Operations - Set
     *
     * @return 结果
     */
    @Override
    public IRedisSetSubOperations opsSet() {
        return this.setOperations;
    }

    /**
     * 获取 Redis Sub Operations - SortSet
     *
     * @return 结果
     */
    @Override
    public IRedisSortSetSubOperations opsSortSet() {
        return this.sortSetOperations;
    }

    /**
     * 获取 Redis Sub Operations - Lua Script
     *
     * @return 结果
     */
    @Override
    public IRedisScriptSubOperations opsScript() {
        return this.scriptOperations;
    }

    // endregion
}
