package com.mylar.sample.modules.blocking.queue;

import java.util.concurrent.*;

/**
 * @author wangz
 * @date 2021/12/8 0008 23:01
 */
public class MyQueue {

    public static void run() throws InterruptedException {

        // 声明一个容量为10的缓存队列
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10);

        //new了三个生产者和一个消费者
        Producer producer1 = new Producer("Producer-1", queue);
        Producer producer2 = new Producer("Producer-2", queue);
        Producer producer3 = new Producer("Producer-3", queue);
        Consumer consumer = new Consumer("Consumer-1", queue);

        // 借助Executors
        ExecutorService service = Executors.newCachedThreadPool();
        // 启动线程
        service.execute(producer1);
        service.execute(producer2);
        service.execute(producer3);
        service.execute(consumer);

        // 执行10s
        Thread.sleep(10 * 1000);
        producer1.stop();
        producer2.stop();
        producer3.stop();

        Thread.sleep(2000);
        // 退出Executor
        service.shutdown();

    }

}
