package com.mylar.sample.modules.thread;

/**
 * @author wangz
 * @date 2022/3/21 0021 21:51
 */
public class CustomThread extends Thread {
    @Override
    public void run() {
        System.out.println("parent thread priority: " + this.getPriority());
        Thread t = new Thread(() -> {
        });
        System.out.println("child thread priority: " + t.getPriority());
    }

    public static void main(String[] args) {
        Thread t = new CustomThread();
        t.setPriority(3);
        t.start();
    }
}
