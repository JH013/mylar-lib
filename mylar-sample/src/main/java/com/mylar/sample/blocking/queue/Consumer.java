package com.mylar.sample.blocking.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author wangz
 * @date 2021/12/8 0008 23:11
 */
public class Consumer implements Runnable {

    private final String name;
    private final BlockingQueue<String> queue;
    private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

    //构造函数
    public Consumer(String name, BlockingQueue<String> queue) {
        this.name = name;
        this.queue = queue;
    }

    public void run() {
        System.out.printf("%s thread start.%n", this.name);
        Random r = new Random();
        boolean isRunning = true;
        try {
            while (isRunning) {
                String data = queue.poll(2, TimeUnit.SECONDS);//有数据时直接从队列的队首取走，无数据时阻塞，在2s内有数据，取走，超过2s还没数据，返回失败
                if (null != data) {
                    System.out.printf("%s fetched data: %s.%n", this.name, data);
                    Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                } else {
                    // 超过2s还没数据，认为所有生产线程都已经退出，自动退出消费线程。
                    isRunning = false;
                    System.out.printf("%s fetched data overtime, prepare quit.%n", this.name);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.printf("%s thread stop.%n", this.name);
        }
    }


}
