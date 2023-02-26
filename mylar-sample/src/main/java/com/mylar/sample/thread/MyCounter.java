package com.mylar.sample.thread;

/**
 * @author wangz
 * @date 2022/3/21 0021 2:24
 */
public class MyCounter {
    int counter;

    public synchronized void increase() {
        counter++;
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        MyCounter myCounter = new MyCounter();

        // 线程1调用同步线程，模拟阻塞
        new Thread(() -> myCounter.increase()).start();

        // 线程2继续调用同步阻塞方法
        Thread thread = new Thread(() -> myCounter.increase());
        thread.start();


        // 让主线程等10毫秒
        Thread.currentThread().sleep(10);

        // 打印线程2，为阻塞状态：BLOCKED
        System.out.println(thread.getState());
    }
}
