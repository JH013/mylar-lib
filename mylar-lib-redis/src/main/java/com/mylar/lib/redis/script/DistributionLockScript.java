package com.mylar.lib.redis.script;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.HashMap;
import java.util.Map;

/**
 * 分布式锁脚本
 *
 * @author wangz
 * @date 2023/3/5 0005 23:18
 */
public class DistributionLockScript {

    // region 单例

    /**
     * 构造方法
     */
    private DistributionLockScript() {
    }

    /**
     * 枚举单例
     *
     * @return 单例
     */
    public static DistributionLockScript singleton() {
        return DistributionLockScript.SingletonEnum.SINGLETON.instance;
    }

    /**
     * 单例枚举
     */
    private enum SingletonEnum {

        /**
         * 单例
         */
        SINGLETON;

        private final DistributionLockScript instance;

        SingletonEnum() {
            DistributionLockScript redisScript = new DistributionLockScript();
            redisScript.initAllScript();
            instance = redisScript;
        }
    }

    //endregion

    // region 变量 & 常量

    /**
     * lua 脚本对象集合
     */
    private Map<String, DefaultRedisScript<?>> redisScripts;

    /**
     * lua 脚本路径 - 分布式锁 - 基础 - 释放锁
     */
    private static final String LUA_DISTRIBUTION_LOCK_BASIC_RELEASE = "scripts/distribution/lock/distribution_lock_basic_release.lua";

    /**
     * lua 脚本路径 - 分布式锁 - 可重入 - 添加锁
     */
    private static final String LUA_DISTRIBUTION_LOCK_REENTRANT_LOCK = "scripts/distribution/lock/distribution_lock_reentrant_lock.lua";

    /**
     * lua 脚本路径 - 分布式锁 - 可重入 - 释放锁
     */
    private static final String LUA_DISTRIBUTION_LOCK_REENTRANT_RELEASE = "scripts/distribution/lock/distribution_lock_reentrant_release.lua";

    // endregion

    // region 公共方法

    /**
     * lua 脚本 - 分布式锁 - 基础 - 释放锁
     */
    public DefaultRedisScript<?> luaDistributionLockBasicRelease() {
        return this.redisScripts.get(LUA_DISTRIBUTION_LOCK_BASIC_RELEASE);
    }

    /**
     * lua 脚本 - 分布式锁 - 可重入 - 添加锁
     */
    public DefaultRedisScript<?> luaDistributionLockReentrantLock() {
        return this.redisScripts.get(LUA_DISTRIBUTION_LOCK_REENTRANT_LOCK);
    }

    /**
     * lua 脚本 - 分布式锁 - 可重入 - 释放锁
     */
    public DefaultRedisScript<?> luaDistributionLockReentrantRelease() {
        return this.redisScripts.get(LUA_DISTRIBUTION_LOCK_REENTRANT_RELEASE);
    }


    // endregion

    // region 私有方法

    /**
     * 初始 lua
     */
    protected void initAllScript() {
        this.redisScripts = new HashMap<>();
        this.initScript(LUA_DISTRIBUTION_LOCK_BASIC_RELEASE, Integer.class);
        this.initScript(LUA_DISTRIBUTION_LOCK_REENTRANT_LOCK, String.class);
        this.initScript(LUA_DISTRIBUTION_LOCK_REENTRANT_RELEASE, String.class);
    }

    /**
     * 初始单个 lua
     */
    protected <T> void initScript(String script, Class<T> resultType) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource(script));
        redisScript.setResultType(resultType);
        redisScripts.put(script, redisScript);
    }

    // endregion
}