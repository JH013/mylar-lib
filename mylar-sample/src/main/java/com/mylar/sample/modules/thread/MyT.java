package com.mylar.sample.modules.thread;

/**
 * @author wangz
 * @date 2022/3/22 0022 22:40
 */
public class MyT extends Thread {

    public MyT(String name) {
        super(name);
    }

    @Override
    public void run() {
        MyLocalVar.setValue("123");
        String value = MyLocalVar.getValue();
        System.out.println("name: " + this.getName() + ", value: " + value);
    }
}
