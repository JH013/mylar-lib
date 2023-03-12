package com.mylar.sample.modules.locking;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangz
 * @date 2022/1/16 0016 0:22
 */
public class MyCounter3 implements Runnable {

    private static int count = 0;

    private static final ReentrantLock _LOCK_ = new ReentrantLock();

    @Override
    public void run() {

        try {
            _LOCK_.lockInterruptibly();      // 加锁
            Thread.sleep(20000);
            count++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            _LOCK_.unlock();                // 解锁
        }
    }
}
