package com.mylar.sample.localthread;

/**
 * @author wangz
 * @date 2022/3/31 0031 23:38
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main BEGIN");

        new ClientThread("Alice").start();
        new ClientThread("Bobby").start();
        new ClientThread("Chris").start();

        Thread.sleep(20000);
        System.out.println("Main END");
    }
}
