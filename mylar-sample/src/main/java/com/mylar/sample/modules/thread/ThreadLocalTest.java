package com.mylar.sample.modules.thread;

/**
 * @author wangz
 * @date 2022/3/22 0022 22:37
 */
public class ThreadLocalTest {

    private static final ThreadLocal<String> localVar = new ThreadLocal<>();

    public static void main(String[] args) {

        new Thread(() -> {
            localVar.set("123");
            System.out.println(localVar.get());
            localVar.remove();
        }).start();

        new Thread(() -> {
            localVar.set("456");
            System.out.println(localVar.get());
            localVar.remove();
        }).start();
    }
}
