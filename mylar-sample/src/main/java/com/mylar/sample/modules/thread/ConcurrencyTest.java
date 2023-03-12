package com.mylar.sample.modules.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * @author wangz
 * @date 2022/3/17 0017 23:23
 */
public class ConcurrencyTest {
    private static final long count = 10000l;

    public static void main(String[] args) throws InterruptedException {
//        concurrency();
//        serial();

        FutureTask futureTask;

        ExecutorService executorService;

        Executors.newFixedThreadPool(2);

        Executors.newSingleThreadExecutor();

        Executors.newCachedThreadPool();

//        Executors.newScheduledThreadPool();

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };

        // 只声明不调用start()方法，得到的状态是NEW
        System.out.println(thread.getState()); // NEW

    }

    private static void concurrency() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int a = 0;
                for (long i = 0; i < count; i++) {
                    a += 5;
                }
            }
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        thread.join();
        System.out.println("concurrency :" + time + "ms,b=" + b);
    }

    private static void serial() {
        long start = System.currentTimeMillis();
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += 5;
        }
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("serial:" + time + "ms,b=" + b + ",a=" + a);
    }
}
