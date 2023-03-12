package com.mylar.sample.modules.thread;

/**
 * @author wangz
 * @date 2022/3/23 0023 1:29
 */
public class ThreadLocalTest2 {

    private static final ThreadLocal<String> local = new ThreadLocal<>();

    private static final InheritableThreadLocal<String> inheritableLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        local.set("123");
        inheritableLocal.set("456");

        new Thread(() -> {
            System.out.println("child thread local val: " + local.get());
            System.out.println("child thread inheritable local val: " + inheritableLocal.get());
        }).start();

        System.out.println("main thread local val: " + local.get());
        System.out.println("main thread inheritable local val: " + inheritableLocal.get());
    }
}
