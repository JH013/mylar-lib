package com.mylar.sample.modules.blocking.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangz
 * @date 2021/12/8 0008 23:04
 */
public class Producer implements Runnable {

    /**
     * 生产者名称
     */
    private final String name;

    /**
     * 阻塞队列
     */
    private final BlockingQueue<String> queue;

    /**
     * 是否运行中
     */
    private volatile boolean isRunning = true;

    /**
     * 计数器
     */
    private static final AtomicInteger COUNTER = new AtomicInteger();

    //构造函数
    public Producer(String name, BlockingQueue<String> queue) {
        this.name = name;
        this.queue = queue;
    }

    @Override
    public void run() {

        String data;

        Random r = new Random();

        System.out.printf("%s thread start.%n", this.name);

        try {

            while (this.isRunning) {

                // 睡眠随机时间
                Thread.sleep(r.nextInt(1000));

                // 生产数据
                data = "[data]-" + COUNTER.incrementAndGet();

                // 设定的等待时间为2s，如果超过2s还没加进去返回true
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.out.printf("%s put data failed, data: %s.%n", this.name, data);
                } else {
                    System.out.printf("%s put data successfully, data: %s.%n", this.name, data);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            System.out.printf("%s thread stop.%n", this.name);
        }
    }

    public void stop() {
        this.isRunning = false;
    }
}
