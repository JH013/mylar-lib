package com.mylar.sample.locking;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangz
 * @date 2022/1/16 0016 2:47
 */
public class ReentrantLockTest2 {

    static Lock lock1 = new ReentrantLock();
    static Lock lock2 = new ReentrantLock();

    public static void main(String[] args) {

        // 先获取锁1，再获取锁2
        Thread thread_1 = new Thread(new ThreadDemo(lock1, lock2));

        // 先获取锁2，再获取锁1
        Thread thread_2 = new Thread(new ThreadDemo(lock2, lock1));

        // 发生死锁
        thread_1.start();
        thread_2.start();

        // 一个线程中断
        thread_1.interrupt();
    }

    static class ThreadDemo implements Runnable {

        Lock firstLock;
        Lock secondLock;

        public ThreadDemo(Lock firstLock, Lock secondLock) {
            this.firstLock = firstLock;
            this.secondLock = secondLock;
        }

        @Override
        public void run() {
            try {
                firstLock.lockInterruptibly();
                TimeUnit.MILLISECONDS.sleep(10);
                secondLock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                firstLock.unlock();
                secondLock.unlock();
                System.out.println(Thread.currentThread().getName() + " end.");
            }
        }
    }
}
