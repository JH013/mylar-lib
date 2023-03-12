package com.mylar.sample.modules.locking;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangz
 * @date 2022/1/16 0016 0:22
 */
public class MyCounter implements Runnable {

    private static int count = 0;

    private static final ReentrantLock _LOCK_ = new ReentrantLock();

    @Override
    public void run() {

        try {
            _LOCK_.lock();     // 加锁
//            _LOCK_.lock();     // 重入锁
            this.increment();
        } finally {
            _LOCK_.unlock();   // 解锁
            _LOCK_.unlock();   // 解锁
            _LOCK_.unlock();   // 解锁
        }
    }

    private void increment() {
        try {
            System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " execute...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        count++;
    }

    public void out() {
        System.out.println("count result: " + count);
    }
}
