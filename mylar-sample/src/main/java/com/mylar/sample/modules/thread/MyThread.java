package com.mylar.sample.modules.thread;

/**
 * @author wangz
 * @date 2022/3/21 0021 21:59
 */
public class MyThread extends Thread {
    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            String name = Thread.currentThread().getName();
            System.out.println(name + " : " + i);
        }
    }

    public static void main(String[] args) {
        Thread threadA = new MyThread("A");
        Thread threadB = new MyThread("B");
        threadA.setPriority(Thread.MIN_PRIORITY);
        threadB.setPriority(Thread.MAX_PRIORITY);
        threadA.start();
        threadB.start();
    }
}
