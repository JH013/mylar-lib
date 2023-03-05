package com.mylar.sample.redis.controller;

import com.mylar.lib.redis.operations.IRedisAggregateOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author wangz
 * @date 2023/3/6 0006 2:10
 */
@RestController
@RequestMapping(value = "/test/distribution/lock")
public class TestDistributionLockController {

    @Autowired
    private IRedisAggregateOperations redisOperations;

    /**
     * Lua List Ret Get
     *
     * @return 结果
     */
    @RequestMapping(value = "/basic")
    public String luaListRetGet(String lockKey, long execTime) {

        long threadId = Thread.currentThread().getId();

        System.out.println("[start   ] time: " + LocalDateTime.now() + " thread: " + threadId);

        boolean success = this.redisOperations.opsDistributionLock().tryLock(lockKey, 2000L, 20000L, () -> {
            try {
                System.out.println("[running ] time: " + LocalDateTime.now() + " thread: " + threadId);
                Thread.sleep(execTime);
                System.out.println("[finished] time: " + LocalDateTime.now() + " thread: " + threadId);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }

            return true;
        });

        System.out.println("[end     ] time: " + LocalDateTime.now() + " thread: " + threadId);

        return success ? "success." : "failed.";
    }
}
