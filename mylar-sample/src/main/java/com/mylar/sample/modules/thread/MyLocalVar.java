package com.mylar.sample.modules.thread;

/**
 * @author wangz
 * @date 2022/3/22 0022 22:42
 */
public class MyLocalVar {

    private static final ThreadLocal<String> localVar = new ThreadLocal<>();

    public static void setValue(String value) {
        localVar.set(value);
    }

    public static String getValue() {
        String value = localVar.get();
        localVar.remove();
        return value;
    }
}
