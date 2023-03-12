package com.mylar.sample.modules.locking;

/**
 * @author wangz
 * @date 2022/1/16 0016 14:27
 */
public class SynchronizedTest2 implements Runnable {

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("interrupt success, exit...");
                return;
            } else {
                System.out.println("executing...");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest2 instance = new SynchronizedTest2();
        Thread thread = new Thread(instance);
        thread.start();
        Thread.sleep(500);
        thread.interrupt();
    }
}
