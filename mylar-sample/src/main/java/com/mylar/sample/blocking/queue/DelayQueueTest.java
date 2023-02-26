package com.mylar.sample.blocking.queue;

import java.time.LocalDateTime;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/**
 * @author wangz
 * @date 2022/3/1 0001 21:28
 */
public class DelayQueueTest {

    /**
     * 声明全局延时队列
     */
    private static DelayQueue delayQueue = new DelayQueue();

    public static void main(String[] args) throws InterruptedException {

        // 向队列中随机添加
        delayQueue.offer(new MyDelayedTask("task1", 50));
        delayQueue.offer(new MyDelayedTask("task2", 30));
        delayQueue.offer(new MyDelayedTask("task3", 5));
        delayQueue.offer(new MyDelayedTask("task4", 10));
        delayQueue.offer(new MyDelayedTask("task5", 25));
        delayQueue.offer(new MyDelayedTask("task6", 60));
        delayQueue.offer(new MyDelayedTask("task7", 80));

        System.out.println("now: " + LocalDateTime.now());
        while (true) {
            Delayed take = delayQueue.take();
            System.out.println(take);
        }
    }
}
