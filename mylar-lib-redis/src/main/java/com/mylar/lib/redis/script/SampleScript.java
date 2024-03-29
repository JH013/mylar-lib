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

    /**
     * lua 脚本路径 - 测试局部变量在函数中使用
     */
    private static final String LUA_LOCAL_FUNC_PARAM = "scripts/sample/local_func_param.lua";

    /**
     * lua 脚本路径 - 测试跨脚本函数调用
     */
    private static final String LUA_CROSS_FUNC_CALL = "scripts/sample/cross_func_call.lua";

    // endregion

    // region 公共方法

    /**
     * lua 脚本 - 测试集合返回
     */
    public DefaultRedisScript<?> luaListRetGet() {
        return this.redisScripts.get(LUA_LIST_RET_GET);
    }

    /**
     * lua 脚本 - 测试局部变量在函数中使用
     */
    public DefaultRedisScript<?> luaLocalFuncParam() {
        return this.redisScripts.get(LUA_LOCAL_FUNC_PARAM);
    }

    /**
     * lua 脚本 - 测试跨脚本函数调用
     */
    public DefaultRedisScript<?> luaCrossFuncCall() {
        return this.redisScripts.get(LUA_CROSS_FUNC_CALL);
    }


    // endregion

    // region 私有方法

    /**
     * 初始 lua
     */
    protected void initAllScript() {
        this.redisScripts = new HashMap<>();
        this.initScript(LUA_LIST_RET_GET, Integer.class);
        this.initScript(LUA_LOCAL_FUNC_PARAM, String.class);
        this.initScript(LUA_CROSS_FUNC_CALL, String.class);
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
