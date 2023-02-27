package com.mylar.lib.redis.script;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.HashMap;
import java.util.Map;

/**
 * 支持Hash过期的缓存脚本
 *
 * @author wangz
 * @date 2023/2/27 0027 1:22
 */
public class HashExpireRedisScript {

    // region 单例

    /**
     * 构造方法
     */
    private HashExpireRedisScript() {
    }

    /**
     * 枚举单例
     *
     * @return 单例
     */
    public static HashExpireRedisScript singleton() {
        return HashExpireRedisScript.SingletonEnum.SINGLETON.instance;
    }

    /**
     * 单例枚举
     */
    private enum SingletonEnum {

        /**
         * 单例
         */
        SINGLETON;

        private final HashExpireRedisScript instance;

        SingletonEnum() {
            HashExpireRedisScript redisScript = new HashExpireRedisScript();
            redisScript.initAllScript();
            instance = redisScript;
        }
    }

    //endregion

    // region 变量 & 常量

    /**
     * lua脚本对象集合
     */
    private Map<String, DefaultRedisScript<?>> redisScripts;

    /**
     * lua 脚本路径 - 批量查询
     */
    private static final String LUA_HASH_EXPIRE_BATCH_GET = "scripts/hash.expire/hash_expire_batch_get.lua";

    /**
     * lua 脚本路径 - 批量更新
     */
    private static final String LUA_HASH_EXPIRE_BATCH_SET = "scripts/hash.expire/hash_expire_batch_set.lua";

    /**
     * lua 脚本路径 - 批量删除 HashKey
     */
    private static final String LUA_HASH_EXPIRE_BATCH_DEL = "scripts/hash.expire/hash_expire_batch_del.lua";

    /**
     * lua 脚本路径 - 删除缓存键
     */
    private static final String LUA_HASH_EXPIRE_KEY_DEL = "scripts/hash.expire/hash_expire_key_del.lua";

    // endregion

    // region 公共方法

    /**
     * lua 脚本 - 批量查询
     */
    public DefaultRedisScript<?> luaBatchGet() {
        return this.redisScripts.get(LUA_HASH_EXPIRE_BATCH_GET);
    }

    /**
     * lua 脚本 - 批量更新
     */
    public DefaultRedisScript<?> luaBatchSet() {
        return this.redisScripts.get(LUA_HASH_EXPIRE_BATCH_SET);
    }

    /**
     * lua 脚本 - 批量删除 HashKey
     */
    public DefaultRedisScript<?> luaBatchDel() {
        return this.redisScripts.get(LUA_HASH_EXPIRE_BATCH_DEL);
    }

    /**
     * lua 脚本 - 删除缓存键
     */
    public DefaultRedisScript<?> luaKeyDel() {
        return this.redisScripts.get(LUA_HASH_EXPIRE_KEY_DEL);
    }

    // endregion

    // region 私有方法

    /**
     * 初始lua
     */
    protected void initAllScript() {
        this.redisScripts = new HashMap<>();
        this.initScript(LUA_HASH_EXPIRE_BATCH_GET, java.util.List.class);
        this.initScript(LUA_HASH_EXPIRE_BATCH_DEL, Integer.class);
        this.initScript(LUA_HASH_EXPIRE_BATCH_SET, Integer.class);
        this.initScript(LUA_HASH_EXPIRE_KEY_DEL, Integer.class);
    }

    /**
     * 初始单个lua
     */
    protected <T> void initScript(String script, Class<T> resultType) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource(script));
        redisScript.setResultType(resultType);
        redisScripts.put(script, redisScript);
    }

    // endregion
}
