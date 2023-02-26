package com.mylar.sample.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author wangz
 * @date 2022/3/21 0021 23:27
 */
public class T1 extends Thread {
    public T1(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println(this.getName() + ".daemon: " + this.isDaemon());
    }

    public static void main(String[] args) throws InterruptedException {

        Thread parentA = new Thread() {
            @Override
            public void run() {
                System.out.println(this.getName() + ".daemon: " + this.isDaemon());
                new T1("childA").start();
            }
        };
        parentA.setName("parentA");
        parentA.setDaemon(false);
        parentA.start();

        Thread parentB = new Thread() {
            @Override
            public void run() {
                System.out.println(this.getName() + ".daemon: " + this.isDaemon());
                new T1("childB").start();
            }
        };
        parentB.setName("parentB");
        parentB.setDaemon(true);
        parentB.start();

        TimeUnit.SECONDS.sleep(3);
    }
}
