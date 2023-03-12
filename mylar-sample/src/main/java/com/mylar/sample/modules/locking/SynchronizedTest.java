package com.mylar.sample.modules.locking;

/**
 * @author wangz
 * @date 2022/1/16 0016 14:27
 */
public class SynchronizedTest implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("interrupt failed.");
                System.out.println("interrupt state: " + Thread.currentThread().isInterrupted());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest instance = new SynchronizedTest();
        Thread thread = new Thread(instance);
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
    }
}
