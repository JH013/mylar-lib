package com.mylar.lib.redis.operations.sub.impl;

import com.mylar.lib.base.utils.HostNameUtils;
import com.mylar.lib.redis.core.RedisTemplateCache;
import com.mylar.lib.redis.operations.sub.IRedisDistributionLockSubOperations;
import com.mylar.lib.redis.operations.sub.IRedisScriptSubOperations;
import com.mylar.lib.redis.operations.sub.base.AbstractRedisSubOperations;
import com.mylar.lib.redis.script.DistributionLockScript;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * Redis Sub Operations - Distribution Lock
 *
 * @author wangz
 * @date 2023/3/6 0006 0:06
 */
public class RedisDistributionLockSubOperations extends AbstractRedisSubOperations implements IRedisDistributionLockSubOperations {

    /**
     * 构造方法
     *
     * @param redisTemplateCache RedisTemplate 缓存
     */
    public RedisDistributionLockSubOperations(RedisTemplateCache redisTemplateCache) {
        super(redisTemplateCache);

        // 初始化 Redis Operations
        this.scriptOperations = new RedisScriptSubOperations(redisTemplateCache);
    }

    // region 变量 & 常量

    /**
     * Redis Sub Operations - Lua Script
     */
    private final IRedisScriptSubOperations scriptOperations;

    /**
     * 加锁成功返回值
     */
    private static final Long LOCK_SUCCESS = 1L;

    /**
     * 释放锁成功返回值
     */
    private static final Long RELEASE_LOCK_SUCCESS = 1L;

    /**
     * 锁自动过期返回值
     */
    private static final Long RELEASE_LOCK_AUTO_EXPIRE = 0L;

    /**
     * 分布式锁的默认锁定超时时间（单位：毫秒）
     */
    private static final Integer DEFAULT_LOCK_TIMEOUT = 20000;

    /**
     * 分布式锁的默认锁过期时间（单位：毫秒）
     */
    private static final Integer DEFAULT_LOCK_EXPIRE = 300;

    // endregion

    // region 接口实现

    /**
     * 尝试加锁
     *
     * @param lockKey  锁定键
     * @param funcExec 业务执行方法
     * @return 是否成功
     */
    @Override
    public boolean tryLock(String lockKey, BooleanSupplier funcExec) {
        return this.tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, DEFAULT_LOCK_EXPIRE, funcExec, false);
    }

    /**
     * 尝试加锁
     *
     * @param lockKey          锁定键
     * @param funcExec         业务执行方法
     * @param supportReentrant 是否支持可重入
     * @return 是否成功
     */
    @Override
    public boolean tryLock(String lockKey, BooleanSupplier funcExec, boolean supportReentrant) {
        return this.tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, DEFAULT_LOCK_EXPIRE, funcExec, supportReentrant);
    }

    /**
     * 尝试加锁
     *
     * @param lockKey     锁定键
     * @param lockTimeout 锁定超时时间（单位：毫秒）
     * @param expireTime  锁过期时间（单位：秒）
     * @param funcExec    业务执行方法
     * @return 是否成功
     */
    @Override
    public boolean tryLock(String lockKey, long lockTimeout, long expireTime, BooleanSupplier funcExec) {
        return this.tryLock(lockKey, lockTimeout, expireTime, funcExec, false);
    }

    /**
     * 尝试加锁
     *
     * @param lockKey          锁定键
     * @param lockTimeout      锁定超时时间（单位：毫秒）
     * @param expireTime       锁过期时间（单位：秒）
     * @param funcExec         业务执行方法
     * @param supportReentrant 是否支持可重入
     * @return 是否成功
     */
    @Override
    public boolean tryLock(String lockKey, long lockTimeout, long expireTime, BooleanSupplier funcExec, boolean supportReentrant) {

        // 默认锁定超时时间 20 秒
        if (lockTimeout <= 0) {
            lockTimeout = DEFAULT_LOCK_TIMEOUT;
        }

        // 默认锁过期时间 300 秒
        if (expireTime <= 0) {
            expireTime = DEFAULT_LOCK_EXPIRE;
        }

        // 开始计时
        long startTime = System.currentTimeMillis();

        // 锁定值，默认为空
        String lockValue = null;
        try {

            // 不断尝试加锁，线程中断，返回失败
            while (!Thread.interrupted()) {

                // 加锁并返回锁定值
                lockValue = this.addLock(lockKey, expireTime, supportReentrant);

                // 加锁成功：执行业务
                if (StringUtils.hasLength(lockValue)) {
                    return funcExec.getAsBoolean();
                }

                // 超过锁定超时时间，终止尝试
                if (System.currentTimeMillis() - startTime > lockTimeout) {
                    break;
                }

                // 当前线程从运行状态转为就绪状态，避免一直占用线程
                Thread.yield();
            }

            // 返回失败
            return false;
        } finally {

            // 加锁成功的情况下释放锁
            if (StringUtils.hasLength(lockValue)) {
                this.releaseLock(lockKey, lockValue, supportReentrant);
            }
        }
    }

    // endregion

    // region 私有方法

    /**
     * 添加锁
     *
     * @param lockKey          锁定键
     * @param expireTime       锁过期时间（单位：秒）
     * @param supportReentrant 是否支持可重入
     * @return 锁定值
     */
    private String addLock(String lockKey, long expireTime, boolean supportReentrant) {

        // 添加锁（可重入）
        if (supportReentrant) {
            return this.addReentrantLock(lockKey, expireTime);
        }

        // 添加锁（基础）
        return this.addBasicLock(lockKey, expireTime);
    }

    /**
     * 添加锁（基础）
     *
     * @param lockKey    锁定键
     * @param expireTime 锁过期时间（单位：秒）
     * @return 锁定值
     */
    private String addBasicLock(String lockKey, long expireTime) {

        // 默认锁过期时间 300 秒
        if (expireTime == 0) {
            expireTime = DEFAULT_LOCK_EXPIRE;
        }

        // 锁定值随机生成
        String lockValue = Double.toString(Math.random());

        // 键不存在时设置缓存值
        Boolean success = this.getTemplate(lockKey).opsForValue().setIfAbsent(lockKey, lockValue, expireTime, TimeUnit.SECONDS);

        // 加锁成功：返回锁定值
        if (Boolean.TRUE.equals(success)) {
            return lockValue;
        }

        // 加锁失败：返回空
        return null;
    }

    /**
     * 添加锁（可重入）
     *
     * @param lockKey    锁定键
     * @param expireTime 锁过期时间（单位：秒）
     * @return 锁定值
     */
    private String addReentrantLock(String lockKey, long expireTime) {

        // 默认锁过期时间 300 秒
        if (expireTime == 0) {
            expireTime = DEFAULT_LOCK_EXPIRE;
        }

        // 键集合
        List<String> keys = Collections.singletonList(lockKey);

        // 参数集合
        List<String> args = new ArrayList<>();

        // 参数 1：锁定值 = 主机名 + 当前线程Id
        String lockValue = HostNameUtils.HOSTNAME + Thread.currentThread().getId();
        args.add(lockValue);

        // 参数 2：锁过期时间
        args.add(String.valueOf(expireTime));

        // 执行脚本：分布式锁 - 可重入 - 添加锁
        Object luaResult = this.scriptOperations.executeScript(DistributionLockScript.singleton().luaDistributionLockReentrantLock(), keys, args);

        // 加锁成功：返回锁定值
        if (LOCK_SUCCESS.equals(luaResult)) {
            return lockValue;
        }

        // 加锁失败：返回空
        return null;
    }

    /**
     * 释放锁
     *
     * @param lockKey          锁定键
     * @param lockValue        锁定值
     * @param supportReentrant 是否支持可重入
     * @return 是否释放成功
     */
    private boolean releaseLock(String lockKey, String lockValue, boolean supportReentrant) {

        // 释放锁（可重入）
        if (supportReentrant) {
            return this.releaseReentrantLock(lockKey, lockValue);
        }

        // 释放锁（基础）
        return this.releaseBasicLock(lockKey, lockValue);
    }

    /**
     * 释放锁（基础）
     *
     * @param lockKey   锁定键
     * @param lockValue 锁定值
     * @return 是否释放成功
     */
    private boolean releaseBasicLock(String lockKey, String lockValue) {

        // 执行脚本：分布式锁 - 简易 - 释放锁
        Object luaResult = this.scriptOperations.executeScript(
                DistributionLockScript.singleton().luaDistributionLockBasicRelease(),
                Collections.singletonList(lockKey),
                Collections.singletonList(lockValue)
        );

        // 释放锁成功 或 锁自动过期
        return RELEASE_LOCK_SUCCESS.equals(luaResult) || RELEASE_LOCK_AUTO_EXPIRE.equals(luaResult);
    }

    /**
     * 释放锁（可重入）
     *
     * @param lockKey   锁定键
     * @param lockValue 锁定值
     * @return 是否释放成功
     */
    private boolean releaseReentrantLock(String lockKey, String lockValue) {

        // 键集合
        List<String> keys = Collections.singletonList(lockKey);

        // 参数集合
        List<String> args = Collections.singletonList(lockValue);

        // 执行脚本：分布式锁 - 可重入 - 释放锁
        Object luaResult = this.scriptOperations.executeScript(DistributionLockScript.singleton().luaDistributionLockReentrantRelease(), keys, args);

        // 释放锁成功 或 锁自动过期
        return RELEASE_LOCK_SUCCESS.equals(luaResult) || RELEASE_LOCK_AUTO_EXPIRE.equals(luaResult);
    }

    // endregion
}
