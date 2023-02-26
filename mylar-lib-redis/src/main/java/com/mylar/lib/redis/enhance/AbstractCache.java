package com.mylar.lib.redis.enhance;

import com.mylar.lib.base.SpringResolver;
import com.mylar.lib.redis.operations.IRedisAggregateOperations;
import org.springframework.beans.factory.InitializingBean;

/**
 * Redis 缓存抽象类
 *
 * @author wangz
 * @date 2023/2/26 0026 15:01
 */
public abstract class AbstractCache implements InitializingBean {

    /**
     * Redis Operations
     */
    protected IRedisAggregateOperations redisOperations;

    /**
     * Init bean
     */
    @Override
    public void afterPropertiesSet() {
        this.redisOperations = SpringResolver.resolve(IRedisAggregateOperations.class);
    }
}
