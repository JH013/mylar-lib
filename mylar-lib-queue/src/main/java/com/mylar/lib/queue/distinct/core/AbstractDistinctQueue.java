package com.mylar.lib.queue.distinct.core;

import com.mylar.lib.base.enhance.SpringResolver;
import com.mylar.lib.redis.operations.IRedisAggregateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 去重队列抽象基类
 *
 * @author wangz
 * @date 2023/12/27 0027 22:54
 */
public abstract class AbstractDistinctQueue<QueueArgs extends IDistinctQueueArgs> {

    // region 构造方法

    /**
     * 构造方法
     *
     * @param queueCode 队列编码
     * @param queueArgs 队列参数
     */
    public AbstractDistinctQueue(String queueCode, QueueArgs queueArgs) {
        this.queueCode = queueCode;
        this.queueArgs = queueArgs;
        this.redisOperations = SpringResolver.resolve(IRedisAggregateOperations.class);
    }

    // endregion

    // region 变量 & 常量

    /**
     * 队列编码
     */
    protected final String queueCode;

    /**
     * 队列参数
     */
    protected final QueueArgs queueArgs;

    /**
     * Redis Operations
     */
    protected final IRedisAggregateOperations redisOperations;

    // endregion

    // region 常量

    /**
     * 日志
     */
    protected static final Logger logger = LoggerFactory.getLogger(AbstractDistinctQueue.class);

    // endregion
}
