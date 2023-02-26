package com.mylar.sample.locking;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangz
 * @date 2022/1/16 0016 0:22
 */
public class MyCounter0 implements Runnable {

    // 临界资源
    private static int count = 0;

    // 锁对象
    private static final ReentrantLock _LOCK_ = new ReentrantLock();

    @Override
    public void run() {
        try {
            _LOCK_.lock();     // 加锁
            count++;           // 同步逻辑
            _LOCK_.lock();     // 重复加锁
        } finally {
            _LOCK_.unlock();   // 解锁
        }
    }
}
