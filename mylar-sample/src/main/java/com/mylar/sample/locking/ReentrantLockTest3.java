package com.mylar.sample.locking;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangz
 * @date 2022/1/16 0016 0:06
 */
public class ReentrantLockTest3 implements Runnable {

    private static final ReentrantLock _LOCK_ = new ReentrantLock();

    private static final Condition _CONDITION_ = _LOCK_.newCondition();

    @Override
    public void run() {
        try {
            _LOCK_.lock();
            System.out.println("wait..." + LocalDateTime.now());
            _CONDITION_.awaitNanos(TimeUnit.SECONDS.toNanos(10));
            System.out.println("complete..." + LocalDateTime.now());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            _LOCK_.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockTest3 instance = new ReentrantLockTest3();
        Thread thread = new Thread(instance);
        thread.start();

        // 尝试唤醒
        Thread.sleep(30000);
        try {
            _LOCK_.lock();
            _CONDITION_.signal();
        } finally {
            _LOCK_.unlock();
        }

        thread.join();
    }
}
