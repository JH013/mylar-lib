package com.mylar.sample.modules.blocking.queue;

import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangz
 * @date 2022/3/4 0004 0:44
 */
public class DelayQueueTest2 {

    public static void main(String[] args) throws InterruptedException {

        ReentrantLock lock = new ReentrantLock();

        Condition condition = lock.newCondition();



//        delayQueue.offer(new MyDelayedTask("task1", 1000000));
//        delayQueue.offer(new MyDelayedTask("task2", 39000));
//        delayQueue.offer(new MyDelayedTask("task3", 19000));
//        delayQueue.offer(new MyDelayedTask("task4", 59000));
//        delayQueue.offer(new MyDelayedTask("task5", 69000));
//        delayQueue.offer(new MyDelayedTask("task6", 79000));
//        delayQueue.offer(new MyDelayedTask("task7", 49000));
//
//        System.out.println(LocalDateTime.now());
//        while (true) {
//            Delayed take = delayQueue.take();
//            System.out.println(take);
//        }
    }
}
