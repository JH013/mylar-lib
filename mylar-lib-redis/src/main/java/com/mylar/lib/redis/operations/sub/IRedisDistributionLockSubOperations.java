package com.mylar.lib.redis.operations.sub;

import java.util.function.BooleanSupplier;

/**
 * Redis Sub Operations - Distribution Lock
 *
 * @author wangz
 * @date 2023/3/6 0006 0:06
 */
public interface IRedisDistributionLockSubOperations {

    /**
     * 尝试加锁
     *
     * @param lockKey  锁定键
     * @param funcExec 业务执行方法
     * @return 是否成功
     */
    boolean tryLock(String lockKey, BooleanSupplier funcExec);

    /**
     * 尝试加锁
     *
     * @param lockKey          锁定键
     * @param funcExec         业务执行方法
     * @param supportReentrant 是否支持可重入
     * @return 是否成功
     */
    boolean tryLock(String lockKey, BooleanSupplier funcExec, boolean supportReentrant);

    /**
     * 尝试加锁
     *
     * @param lockKey     锁定键
     * @param lockTimeout 锁定超时时间（单位：秒）
     * @param expireTime  锁过期时间（单位：秒）
     * @param funcExec    业务执行方法
     * @return 是否成功
     */
    boolean tryLock(String lockKey, long lockTimeout, long expireTime, BooleanSupplier funcExec);

    /**
     * 尝试加锁
     *
     * @param lockKey          锁定键
     * @param lockTimeout      锁定超时时间（单位：秒）
     * @param expireTime       锁过期时间（单位：秒）
     * @param funcExec         业务执行方法
     * @param supportReentrant 是否支持可重入
     * @return 是否成功
     */
    boolean tryLock(String lockKey, long lockTimeout, long expireTime, BooleanSupplier funcExec, boolean supportReentrant);
}
