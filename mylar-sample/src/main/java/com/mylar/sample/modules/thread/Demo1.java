package com.mylar.sample.modules.thread;

/**
 * @author wangz
 * @date 2022/3/21 0021 23:02
 */
public class Demo1 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println(this.isDaemon() ?
                        "daemon thread run." : "user thread run.");
                while (true) ;
            }
        };
        thread.setDaemon(true);
        thread.start();
        Thread.sleep(1000);
        System.out.println("main thread end.");
    }
}
