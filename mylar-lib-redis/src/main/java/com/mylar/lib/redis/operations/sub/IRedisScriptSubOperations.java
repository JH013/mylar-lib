package com.mylar.lib.redis.operations.sub;

import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;

/**
 * Redis Sub Operations - Lua Script
 *
 * @author wangz
 * @date 2023/2/26 0026 22:36
 */
public interface IRedisScriptSubOperations {

    /**
     * 执行脚本
     * <p>
     * 1、入参 cacheKey 用来映射服务器连接
     *
     * @param cacheKey 缓存键
     * @param script   lua脚本
     * @param keyCount 缓存键参数个数
     * @param params   参数集合（缓存键在前，其他参数在后，与脚本顺序对应）
     * @param <T>      泛型
     * @return 执行结果
     */
    <T> Object executeScript(String cacheKey, RedisScript<T> script, final int keyCount, final String... params);

    /**
     * 执行脚本
     * <p>
     * 1、入参 cacheKey 用来映射服务器连接
     *
     * @param cacheKey 缓存键
     * @param script   lua脚本
     * @param keys     缓存键参数
     * @param args     其他参数
     * @return 执行结果
     */
    <T> Object executeScript(String cacheKey, RedisScript<T> script, final List<String> keys, final List<String> args);

    /**
     * 执行脚本
     * <p>
     * 1、根据缓存键参数中第一个作为 cacheKey 用来映射服务器连接
     *
     * @param script lua脚本
     * @param keys   缓存键参数
     * @param args   其他参数
     * @return 执行结果
     */
    <T> Object executeScript(RedisScript<T> script, final List<String> keys, final List<String> args);

    /**
     * 执行脚本
     * <p>
     * 1、根据缓存键参数中第一个作为 cacheKey 用来映射服务器连接
     * 2、无其他参数
     *
     * @param script lua脚本
     * @param keys   缓存键参数
     * @return 执行结果
     */
    <T> Object executeScript(RedisScript<T> script, final List<String> keys);
}
