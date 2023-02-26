package com.mylar.lib.redis.ext;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.HashMap;
import java.util.Map;

/**
 * List缓存脚本
 *
 * @author wangz
 * @date 2023/2/26 0026 23:26
 */
public class ListRedisScript {

    // region 单例

    /**
     * 构造方法
     */
    private ListRedisScript() {
    }

    /**
     * 枚举单例
     *
     * @return 单例
     */
    public static ListRedisScript singleton() {
        return ListRedisScript.SingletonEnum.SINGLETON.instance;
    }

    /**
     * 单例枚举
     */
    private enum SingletonEnum {

        /**
         * 单例
         */
        SINGLETON;

        private final ListRedisScript instance;

        SingletonEnum() {
            ListRedisScript redisScript = new ListRedisScript();
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
     * lua 脚本路径（List 缓存全量同步）
     */
    private static final String LUA_LIST_SYNC_FULL = "scripts/list/list_sync_full.lua";

    // endregion

    // region 公共方法

    /**
     * lua脚本对象,过期hash批量查询
     */
    public DefaultRedisScript<?> luaListSyncFull() {
        return redisScripts.get(LUA_LIST_SYNC_FULL);
    }

    // endregion

    // region 私有方法

    /**
     * 初始lua
     */
    protected void initAllScript() {
        redisScripts = new HashMap<>();
        initScript(LUA_LIST_SYNC_FULL, Integer.class);
    }

    /**
     * 初始单个lua
     */
    protected void initScript(String script, Class<?> resultType) {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource(script));
        redisScript.setResultType(resultType);
        redisScripts.put(script, redisScript);
    }

    // endregion
}
