package com.mylar.lib.redis.operations.sub.impl;

import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.operations.sub.IRedisScriptSubOperations;
import com.mylar.lib.redis.operations.sub.base.AbstractRedisSubOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis Sub Operations - Lua Script
 *
 * @author wangz
 * @date 2023/2/26 0026 22:37
 */
public class RedisScriptSubOperations extends AbstractRedisSubOperations implements IRedisScriptSubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public RedisScriptSubOperations(RedisTemplateCache redisTemplateCache) {
        super(redisTemplateCache);
    }

    // region 接口实现

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
    @Override
    public <T> Object executeScript(String cacheKey, RedisScript<T> script, final int keyCount, final String... params) {

        // 获取 RedisTemplate
        StringRedisTemplate redisTemplate = this.getTemplate(cacheKey);
        try {

            // 按 lua 脚本的 sha1 校验和执行
            return this.executeByScriptSha(redisTemplate, script.getSha1(), keyCount, params);
        } catch (Throwable e) {

            // 脚本不存在时，按 lua 脚本执行
            if (this.noScriptException(e)) {
                return this.executeByScript(redisTemplate, script.getScriptAsString(), keyCount, params);
            }

            throw e;
        }
    }

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
    @Override
    public <T> Object executeScript(String cacheKey, RedisScript<T> script, final List<String> keys, final List<String> args) {

        // 新建集合，避免入参集合不可更改而导致异常
        List<String> paramsList = args == null ? new ArrayList<>() : new ArrayList<>(args);
        paramsList.addAll(0, keys);

        // 执行脚本
        return this.executeScript(cacheKey, script, keys.size(), paramsList.toArray(new String[0]));
    }

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
    @Override
    public <T> Object executeScript(RedisScript<T> script, final List<String> keys, final List<String> args) {

        // 缓存键参数不可为空
        if (keys.size() <= 0) {
            throw new RuntimeException("Lua script must contains key.");
        }

        // 执行脚本
        return this.executeScript(keys.get(0), script, keys, args);
    }

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
    @Override
    public <T> Object executeScript(RedisScript<T> script, final List<String> keys) {
        return this.executeScript(keys.get(0), script, keys, null);
    }

    // endregion

    // region 私有方法

    /**
     * 执行脚本
     * <p>
     * 1、支持集群和单点
     * 2、没有直接使用 RedisTemplate.execute(RedisScript<T> script, List<K> keys, Object... args) 的原因：集群模式调用脚本时不支持
     *
     * @param redisTemplate RedisTemplate
     * @param script        lua脚本
     * @param keyCount      缓存键参数个数
     * @param params        参数集合（缓存键在前，其他参数在后，与脚本顺序对应）
     * @return 执行结果
     */
    private Object executeByScript(final StringRedisTemplate redisTemplate, final String script, final int keyCount, final String... params) {
        return redisTemplate.execute((RedisCallback<Object>) connection -> {
            Object nativeConnection = connection.getNativeConnection();

            // 集群模式
            if (nativeConnection instanceof JedisCluster) {
                return ((JedisCluster) nativeConnection).eval(script, keyCount, params);
            }
            // 单机模式
            else if (nativeConnection instanceof Jedis) {
                return ((Jedis) nativeConnection).eval(script, keyCount, params);
            }

            return null;
        });
    }

    /**
     * 执行脚本（sha1 校验和）
     * <p>
     * 1、支持集群和单点
     * 2、没有直接使用 RedisTemplate.execute(RedisScript<T> script, List<K> keys, Object... args) 的原因：集群模式调用脚本时不支持
     *
     * @param redisTemplate RedisTemplate
     * @param sha1          lua脚本sha1校验和
     * @param keyCount      缓存键参数个数
     * @param params        参数集合（缓存键在前，其他参数在后，与脚本顺序对应）
     * @return 执行结果
     */
    private Object executeByScriptSha(final StringRedisTemplate redisTemplate, final String sha1, final int keyCount, final String... params) {
        return redisTemplate.execute((RedisCallback<Object>) connection -> {
            Object nativeConnection = connection.getNativeConnection();

            // 集群模式
            if (nativeConnection instanceof JedisCluster) {
                return ((JedisCluster) nativeConnection).evalsha(sha1, keyCount, params);
            }
            // 单机模式
            else if (nativeConnection instanceof Jedis) {
                return ((Jedis) nativeConnection).evalsha(sha1, keyCount, params);
            }

            return null;
        });
    }

    /**
     * 是否是脚本不存在异常
     *
     * @param e 异常
     * @return 结果
     */
    private boolean noScriptException(Throwable e) {
        Throwable current = e;
        while (current != null) {
            String exMessage = current.getMessage();
            if (exMessage != null && exMessage.contains("NOSCRIPT")) {
                return true;
            }

            current = current.getCause();
        }

        return false;
    }

    // endregion
}
