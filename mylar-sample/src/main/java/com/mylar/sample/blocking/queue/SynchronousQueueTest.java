package com.mylar.sample.blocking.queue;

import java.time.LocalDateTime;
import java.util.concurrent.SynchronousQueue;

/**
 * @author wangz
 * @date 2022/3/13 0013 18:45
 */
public class SynchronousQueueTest {

    public static void main(String[] args) throws InterruptedException {
        final SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        Thread putThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    System.out.println("put start, time: " + LocalDateTime.now());
                    queue.put(1);
                    System.out.println("put end, time: " + LocalDateTime.now());
                } catch (InterruptedException e) {
                }
            }
        });

        Thread takeThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(6000);
                    System.out.println("take start, time: " + LocalDateTime.now());
                    queue.take();
                    System.out.println("take end, time: " + LocalDateTime.now());
                } catch (InterruptedException e) {
                }
            }
        });

        putThread.start();
        Thread.sleep(1000);
        takeThread.start();
    }
}