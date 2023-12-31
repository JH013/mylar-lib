package com.mylar.lib.queue.script;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 队列 LUA 脚本
 *
 * @author wangz
 * @date 2023/12/27 0027 22:25
 */
public class QueueScript {

    // region 单例

    /**
     * 构造方法
     */
    private QueueScript() {
    }

    /**
     * 枚举单例
     *
     * @return 单例
     */
    public static QueueScript singleton() {
        return SingletonEnum.SINGLETON.instance;
    }

    /**
     * 单例枚举
     */
    private enum SingletonEnum {

        /**
         * 单例
         */
        SINGLETON;

        private final QueueScript instance;

        SingletonEnum() {
            QueueScript redisScript = new QueueScript();
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
     * lua 脚本路径 - 简单去重队列入队
     */
    private static final String LUA_SIMPLE_DISTINCT_ENQUEUE = "scripts/simple_distinct_enqueue.lua";

    /**
     * lua 脚本路径 - 简单去重队列出队
     */
    private static final String LUA_SIMPLE_DISTINCT_DEQUEUE = "scripts/simple_distinct_dequeue.lua";

    /**
     * lua 脚本路径 - 简单去重队列删除
     */
    private static final String LUA_SIMPLE_DISTINCT_REMOVE = "scripts/simple_distinct_remove.lua";

    /**
     * lua 脚本路径 - 并发去重队列入队
     */
    private static final String LUA_CONCURRENT_DISTINCT_ENQUEUE = "scripts/concurrent_distinct_enqueue.lua";

    /**
     * lua 脚本路径 - 并发去重队列出队
     */
    private static final String LUA_CONCURRENT_DISTINCT_DEQUEUE = "scripts/concurrent_distinct_dequeue.lua";

    /**
     * lua 脚本路径 - 并发去重队列删除等待中数据
     */
    private static final String LUA_CONCURRENT_DISTINCT_REMOVE_WAITING = "scripts/concurrent_distinct_remove_waiting.lua";

    /**
     * lua 脚本路径 - 并发去重队列删除执行中数据
     */
    private static final String LUA_CONCURRENT_DISTINCT_REMOVE_RUNNING = "scripts/concurrent_distinct_remove_running.lua";

    // endregion

    // region 公共方法

    /**
     * lua 脚本 - 简单去重队列入队
     */
    public DefaultRedisScript<?> luaSimpleDistinctEnqueue() {
        return this.redisScripts.get(LUA_SIMPLE_DISTINCT_ENQUEUE);
    }

    /**
     * lua 脚本 - 简单去重队列出队
     */
    public DefaultRedisScript<?> luaSimpleDistinctDequeue() {
        return this.redisScripts.get(LUA_SIMPLE_DISTINCT_DEQUEUE);
    }

    /**
     * lua 脚本 - 去重队列删除
     */
    public DefaultRedisScript<?> luaSimpleDistinctRemove() {
        return this.redisScripts.get(LUA_SIMPLE_DISTINCT_REMOVE);
    }

    /**
     * lua 脚本 - 并发去重队列入队
     */
    public DefaultRedisScript<?> luaConcurrentDistinctEnqueue() {
        return this.redisScripts.get(LUA_CONCURRENT_DISTINCT_ENQUEUE);
    }

    /**
     * lua 脚本 - 并发去重队列出队
     */
    public DefaultRedisScript<?> luaConcurrentDistinctDequeue() {
        return this.redisScripts.get(LUA_CONCURRENT_DISTINCT_DEQUEUE);
    }

    /**
     * lua 脚本 - 并发去重队列删除等待中数据
     */
    public DefaultRedisScript<?> luaConcurrentDistinctRemoveWaiting() {
        return this.redisScripts.get(LUA_CONCURRENT_DISTINCT_REMOVE_WAITING);
    }

    /**
     * lua 脚本 - 并发去重队列删除执行中数据
     */
    public DefaultRedisScript<?> luaConcurrentDistinctRemoveRunning() {
        return this.redisScripts.get(LUA_CONCURRENT_DISTINCT_REMOVE_RUNNING);
    }

    // endregion

    // region 私有方法

    /**
     * 初始 lua
     */
    protected void initAllScript() {
        this.redisScripts = new HashMap<>();
        this.initScript(LUA_SIMPLE_DISTINCT_ENQUEUE, Integer.class);
        this.initScript(LUA_SIMPLE_DISTINCT_DEQUEUE, List.class);
        this.initScript(LUA_SIMPLE_DISTINCT_REMOVE, Integer.class);
        this.initScript(LUA_CONCURRENT_DISTINCT_ENQUEUE, Integer.class);
        this.initScript(LUA_CONCURRENT_DISTINCT_DEQUEUE, List.class);
        this.initScript(LUA_CONCURRENT_DISTINCT_REMOVE_WAITING, Integer.class);
        this.initScript(LUA_CONCURRENT_DISTINCT_REMOVE_RUNNING, Integer.class);
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
