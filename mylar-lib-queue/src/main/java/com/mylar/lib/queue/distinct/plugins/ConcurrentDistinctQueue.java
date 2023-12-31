package com.mylar.lib.queue.distinct.plugins;

import com.mylar.lib.queue.distinct.core.AbstractDistinctQueue;
import com.mylar.lib.queue.distinct.core.IConcurrentDistinctQueue;
import com.mylar.lib.queue.distinct.data.ConcurrentDistinctQueueArgs;
import com.mylar.lib.queue.distinct.data.EnqueueStatusEnum;
import com.mylar.lib.queue.script.QueueScript;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 并发去重队列
 *
 * @author wangz
 * @date 2023/12/27 0027 22:54
 */
public class ConcurrentDistinctQueue extends AbstractDistinctQueue<ConcurrentDistinctQueueArgs> implements IConcurrentDistinctQueue {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param queueCode 队列编码
     * @param queueArgs 队列参数
     */
    public ConcurrentDistinctQueue(String queueCode, ConcurrentDistinctQueueArgs queueArgs) {
        super(queueCode, queueArgs);

        // 等待中缓存键集合
        this.waitingKeys = new ArrayList<>();

        // 等待中队列缓存键（list）
        this.waitingKeys.add(String.format("{dq:concurrent:%s}:wait_queue", this.queueCode));

        // 等待中数据缓存键（hash）
        this.waitingKeys.add(String.format("{dq:concurrent:%s}:wait_data", this.queueCode));

        // 执行中缓存键集合
        this.runningKeys = new ArrayList<>();

        // 执行中数据缓存键（hash）
        this.runningKeys.add(String.format("{dq:concurrent:%s}:exec_data", this.queueCode));
    }

    // endregion

    // region 变量

    /**
     * 等待中缓存键集合
     */
    private final List<String> waitingKeys;

    /**
     * 执行中缓存键集合
     */
    private final List<String> runningKeys;

    // endregion

    // region 接口实现

    /**
     * 入队
     *
     * @param dataId      数据ID
     * @param dataContent 数据内容
     * @return 入队状态
     */
    @Override

    public EnqueueStatusEnum enqueue(String dataId, String dataContent) {

        // 校验入参
        if (StringUtils.isEmpty(dataId) || StringUtils.isEmpty(dataContent)) {
            return EnqueueStatusEnum.EXCEPTION;
        }

        try {

            // LUA 脚本
            DefaultRedisScript<?> script = QueueScript.singleton().luaConcurrentDistinctEnqueue();

            // 键集合
            List<String> keys = new ArrayList<>();
            keys.addAll(this.waitingKeys);
            keys.addAll(this.runningKeys);

            // 值集合
            List<String> args = new ArrayList<>();

            // 数据ID
            args.add(dataId);

            // 数据内容
            args.add(dataContent);

            // 等待中数据的数量上限
            args.add(String.valueOf(this.queueArgs.getWaitingDataCapacity()));

            // 等待中数据的过期时间（单位：秒）
            args.add(String.valueOf(this.queueArgs.getWaitingDataExpire()));

            // 执行中数据的缓存键后缀
            args.add(this.queueArgs.getRunningDataKeySuffix());

            // 当等待中已存在时更新数据内容
            args.add(this.queueArgs.isUpdateWhenWaitingExist() ? "1" : "0");

            // 执行脚本
            Object luaResult = this.redisOperations.opsScript().executeScript(script, keys, args);

            // 转换结果
            return EnqueueStatusEnum.create(Integer.parseInt(luaResult.toString()));
        } catch (Exception e) {
            logger.error("[并发去重队列]入队异常", e);
            return EnqueueStatusEnum.EXCEPTION;
        }
    }

    /**
     * 出队
     *
     * @param dequeueCount 出队数量
     * @return 出队数据
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> dequeue(int dequeueCount) {

        // 默认出队数量 1
        if (dequeueCount <= 0) {
            dequeueCount = 1;
        }

        // 结果集
        Map<String, String> ret = new HashMap<>();
        try {

            // LUA 脚本
            DefaultRedisScript<?> script = QueueScript.singleton().luaConcurrentDistinctDequeue();

            // 键集合
            List<String> keys = new ArrayList<>();
            keys.addAll(this.waitingKeys);
            keys.addAll(this.runningKeys);

            // 值集合
            List<String> args = new ArrayList<>();

            // 执行中数据的数量上限
            args.add(String.valueOf(this.queueArgs.getRunningDataCapacity()));

            // 执行中数据的过期时间（单位：秒）
            args.add(String.valueOf(this.queueArgs.getRunningDataExpire()));

            // 执行中数据的缓存键后缀
            args.add(this.queueArgs.getRunningDataKeySuffix());

            // 出队数量
            args.add(String.valueOf(dequeueCount));

            // 执行脚本
            Object luaResult = this.redisOperations.opsScript().executeScript(script, keys, args);

            // 结果为空
            List<String> listResult = (List<String>) luaResult;
            if (listResult == null || listResult.size() == 0) {
                return ret;
            }

            // 封装结果集
            int size = (listResult.size() / 2) * 2;
            for (int i = 0; i < size; i = i + 2) {
                ret.put(listResult.get(i), listResult.get(i + 1));
            }
        } catch (Exception e) {
            logger.error("[简单去重队列]出队异常", e);
        }

        // 返回结果集
        return ret;
    }

    /**
     * 删除等待中数据
     *
     * @param dataIds 数据ID集合
     * @return 删除数量
     */
    @Override
    public int removeWaiting(List<String> dataIds) {

        // 校验入参
        if (CollectionUtils.isEmpty(dataIds)) {
            return 0;
        }

        // 删除数量
        int ret = 0;
        try {

            // LUA 脚本
            DefaultRedisScript<?> script = QueueScript.singleton().luaConcurrentDistinctRemoveWaiting();

            // 值集合
            List<String> args = new ArrayList<>(dataIds);

            // 执行脚本
            Object luaResult = this.redisOperations.opsScript().executeScript(script, this.waitingKeys, args);

            // 删除数量
            return Integer.parseInt(luaResult.toString());
        } catch (Exception e) {
            logger.error("[简单去重队列]删除异常", e);
            return ret;
        }
    }

    /**
     * 删除执行中数据
     *
     * @param dataIds 数据ID集合
     * @return 删除数量
     */
    @Override
    public int removeRunning(List<String> dataIds) {

        // 校验入参
        if (CollectionUtils.isEmpty(dataIds)) {
            return 0;
        }

        // 删除数量
        int ret = 0;
        try {

            // LUA 脚本
            DefaultRedisScript<?> script = QueueScript.singleton().luaConcurrentDistinctRemoveRunning();

            // 值集合
            List<String> args = new ArrayList<>();

            // 执行中数据的缓存键后缀
            args.add(this.queueArgs.getRunningDataKeySuffix());

            // 数据ID集合
            args.addAll(dataIds);

            // 执行脚本
            Object luaResult = this.redisOperations.opsScript().executeScript(script, this.runningKeys, args);

            // 删除数量
            return Integer.parseInt(luaResult.toString());
        } catch (Exception e) {
            logger.error("[简单去重队列]删除异常", e);
            return ret;
        }
    }

    // endregion
}
