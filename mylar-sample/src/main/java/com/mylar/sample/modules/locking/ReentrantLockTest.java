package com.mylar.sample.modules.locking;

import java.time.LocalDateTime;

/**
 * @author wangz
 * @date 2022/1/16 0016 0:06
 */
public class ReentrantLockTest {

    public static void main(String[] args) throws InterruptedException {

//        testReentrantLock();

        testReentrantLockInterrupt();
    }

    public static void testReentrantLock() throws InterruptedException {

        // 开始时间
        System.out.println(LocalDateTime.now() + " program start...");

        // 创建实例
        MyCounter counter = new MyCounter();

        // 启动 3 个线程执行
        Thread thread_1 = new Thread(counter);
        Thread thread_2 = new Thread(counter);
        Thread thread_3 = new Thread(counter);

        thread_1.start();
        thread_2.start();
        thread_3.start();

        thread_1.join();
        thread_2.join();
        thread_3.join();

        // 输出结果
        counter.out();

        // 结束时间
        System.out.println(LocalDateTime.now() + " program stop...");
    }

    public static void testReentrantLockOverTime() throws InterruptedException {

        // 开始时间
        System.out.println(LocalDateTime.now() + " program start...");

        // 创建实例
        MyCounter2 counter = new MyCounter2();

        // 启动 3 个线程执行
        Thread thread_1 = new Thread(counter);
        Thread thread_2 = new Thread(counter);
        Thread thread_3 = new Thread(counter);

        thread_1.start();
        thread_2.start();
        thread_3.start();

        thread_1.join();
        thread_2.join();
        thread_3.join();

        // 输出结果
        counter.out();

        // 结束时间
        System.out.println(LocalDateTime.now() + " program stop...");
    }

    public static void testReentrantLockInterrupt() throws InterruptedException {

        // 开始时间
        System.out.println(LocalDateTime.now() + " program start...");

        // 创建实例
        MyCounter3 counter = new MyCounter3();

        // 启动 2 个线程执行
        Thread thread_1 = new Thread(counter);
        Thread thread_2 = new Thread(counter);

        thread_1.start();
        thread_2.start();

        // 运行 5 秒后一个线程主动中断
        Thread.sleep(5000);
        thread_1.interrupt();

        thread_1.join();
        thread_2.join();

        // 结束时间
        System.out.println(LocalDateTime.now() + " program stop...");
    }
}
