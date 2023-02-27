package com.mylar.lib.redis.operations;

import com.mylar.lib.redis.operations.sub.*;

/**
 * Redis Operations 聚合
 *
 * @author wangz
 * @date 2023/2/26 0026 4:43
 */
public interface IRedisAggregateOperations {

    /**
     * 获取 Redis Sub Operations - Key
     *
     * @return 结果
     */
    IRedisKeySubOperations opsKey();

    /**
     * 获取 Redis Sub Operations - String
     *
     * @return 结果
     */
    IRedisStringSubOperations opsString();

    /**
     * 获取 Redis Sub Operations - Hash
     *
     * @return 结果
     */
    IRedisHashSubOperations opsHash();

    /**
     * 获取 Redis Sub Operations - List
     *
     * @return 结果
     */
    IRedisListSubOperations opsList();

    /**
     * 获取 Redis Sub Operations - Set
     *
     * @return 结果
     */
    IRedisSetSubOperations opsSet();

    /**
     * 获取 Redis Sub Operations - SortSet
     *
     * @return 结果
     */
    IRedisSortSetSubOperations opsSortSet();

    /**
     * 获取 Redis Sub Operations - Lua Script
     *
     * @return 结果
     */
    IRedisScriptSubOperations opsScript();

    /**
     * 获取 Redis Sub Operations - Hash Expire
     *
     * @return 结果
     */
    IRedisHashExpireSubOperations opsHashExpire();
}
