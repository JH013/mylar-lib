package com.mylar.sample.modules.locking;

/**
 * @author wangz
 * @date 2022/1/16 0016 14:27
 */
public class SynchronizedTest4 implements Runnable {

    private static final Object _LOCK_ = new Object();

    @Override
    public void run() {
        try {
            // 执行前
            String name = Thread.currentThread().getName();
            System.out.println(name + " invoke before.");

            // 执行中（等待唤醒）
            synchronized (_LOCK_) {
                System.out.println(name + " execute wait.");
                _LOCK_.wait();
            }

            // 执行后
            System.out.println(name + " invoke after.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void executeNotify() {
        synchronized (_LOCK_) {
            _LOCK_.notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest4 instance = new SynchronizedTest4();
        Thread thread_1 = new Thread(instance);
        Thread thread_2 = new Thread(instance);
        Thread thread_3 = new Thread(instance);

        thread_1.start();
        thread_2.start();
        thread_3.start();

        Thread.sleep(2000);
        instance.executeNotify();

        thread_1.join();
        thread_2.join();
        thread_3.join();
    }
}
