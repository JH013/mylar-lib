package com.mylar.sample.modules.locking;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangz
 * @date 2022/1/16 0016 0:22
 */
public class MyCounter2 implements Runnable {

    private static int count = 0;

    private static final ReentrantLock _LOCK_ = new ReentrantLock();

    @Override
    public void run() {
        try {
            if (_LOCK_.tryLock(1000, TimeUnit.MILLISECONDS)) {
                try {
                    this.increment();
                } finally {
                    _LOCK_.unlock();
                }
            } else {
                System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " lock failed...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
