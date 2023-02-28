package com.mylar.lib.redis.script;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例脚本
 *
 * @author wangz
 * @date 2023/3/1 0001 0:05
 */
public class SampleScript {

    // region 单例

    /**
     * 构造方法
     */
    private SampleScript() {
    }

    /**
     * 枚举单例
     *
     * @return 单例
     */
    public static SampleScript singleton() {
        return SampleScript.SingletonEnum.SINGLETON.instance;
    }

    /**
     * 单例枚举
     */
    private enum SingletonEnum {

        /**
         * 单例
         */
        SINGLETON;

        private final SampleScript instance;

        SingletonEnum() {
            SampleScript redisScript = new SampleScript();
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
     * lua 脚本路径 - 测试集合返回
     */
    private static final String LUA_LIST_RET_GET = "scripts/sample/list_ret_get.lua";

    // endregion

    // region 公共方法

    /**
     * lua 脚本 -测试集合返回
     */
    public DefaultRedisScript<?> luaListRetGet() {
        return this.redisScripts.get(LUA_LIST_RET_GET);
    }

    // endregion

    // region 私有方法

    /**
     * 初始 lua
     */
    protected void initAllScript() {
        this.redisScripts = new HashMap<>();
        this.initScript(LUA_LIST_RET_GET, Integer.class);
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
