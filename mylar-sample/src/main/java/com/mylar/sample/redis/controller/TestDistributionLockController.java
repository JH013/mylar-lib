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
     * Basic Lock
     *
     * @return 结果
     */
    @RequestMapping(value = "/basicLock")
    public String basicLock(String lockKey, long execTime) {

        long threadId = Thread.currentThread().getId();

        System.out.println("[start   ] time: " + LocalDateTime.now() + " thread: " + threadId);

        boolean success = this.redisOperations.opsDistributionLock().tryLock(lockKey, 2000L, 20L, () -> {
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

    /**
     * Reentrant Lock
     *
     * @return 结果
     */
    @RequestMapping(value = "/reentrantLock")
    public String reentrantLock(String lockKey, long execTime) {

        long threadId = Thread.currentThread().getId();

        System.out.println("[outer] [start   ] time: " + LocalDateTime.now() + " thread: " + threadId);

        final boolean[] innerSuccess = new boolean[1];
        boolean outerSuccess = this.redisOperations.opsDistributionLock().tryLock(lockKey, 2000L, 20L, () -> {
            try {
                System.out.println("[outer] [running ] time: " + LocalDateTime.now() + " thread: " + threadId);
                Thread.sleep(execTime);
                System.out.println("[outer] [finished] time: " + LocalDateTime.now() + " thread: " + threadId);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }

            innerSuccess[0] = this.redisOperations.opsDistributionLock().tryLock(lockKey, 2000L, 20L, () -> {
                try {
                    System.out.println("[inner] [running ] time: " + LocalDateTime.now() + " thread: " + threadId);
                    Thread.sleep(execTime);
                    System.out.println("[inner] [finished] time: " + LocalDateTime.now() + " thread: " + threadId);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

                return true;
            }, true);

            return true;
        }, true);

        System.out.println("[outer] [end     ] time: " + LocalDateTime.now() + " thread: " + threadId);

        return (outerSuccess ? "outer success, " : "outer failed, ") + (innerSuccess[0] ? "inner success." : "inner failed.");
    }
}
