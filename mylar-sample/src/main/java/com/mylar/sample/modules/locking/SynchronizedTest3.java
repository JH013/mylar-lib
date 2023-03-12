package com.mylar.sample.modules.locking;

/**
 * @author wangz
 * @date 2022/1/16 0016 14:27
 */
public class SynchronizedTest3 implements Runnable {

    private final Object lockFirst;
    private final Object lockSecond;

    public SynchronizedTest3(Object lockFirst, Object lockSecond) {
        this.lockFirst = lockFirst;
        this.lockSecond = lockSecond;
    }

    @Override
    public void run() {

        synchronized (this.lockFirst) {
            String threadName = Thread.currentThread().getName();
            try {
                Thread.sleep(2000);
                System.out.println(threadName + " execute after first lock.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this.lockSecond) {
                System.out.println(threadName + " execute after second lock.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Object lock_1 = new Object();
        Object lock_2 = new Object();

        SynchronizedTest3 instance_1 = new SynchronizedTest3(lock_1, lock_2);
        Thread thread_1 = new Thread(instance_1);

        SynchronizedTest3 instance_2 = new SynchronizedTest3(lock_2, lock_1);
        Thread thread_2 = new Thread(instance_2);

        thread_1.start();
        thread_2.start();

        Thread.sleep(5000);
        thread_2.interrupt();
        System.out.println("try interrupt.");

        thread_1.join();
        thread_2.join();
    }
}
