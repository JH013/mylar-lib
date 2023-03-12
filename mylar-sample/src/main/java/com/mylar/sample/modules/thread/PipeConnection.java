package com.mylar.sample.modules.thread;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author wangz
 * @date 2022/3/22 0022 0:24
 */
public class PipeConnection {
    public static void main(String[] args) {

        // 创建管道输出流
        PipedOutputStream pos = new PipedOutputStream();

        // 创建管道输入流
        PipedInputStream pis = new PipedInputStream();
        try {

            //将管道输入流与输出流连接
            pos.connect(pis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建生产者线程
        Producer p = new Producer(pos);

        // 创建消费者线程
        Consumer c1 = new Consumer(pis);

        // 启动线程
        p.start();
        c1.start();
    }
}

/**
 * 生产者线程(与一个管道输入流相关联)
 */
class Producer extends Thread {

    private PipedOutputStream pos;

    public Producer(PipedOutputStream pos) {
        this.pos = pos;
    }

    public void run() {
        int i = 0;
        try {
            while (true) {
                this.sleep(3000);
                pos.write(i);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * 消费者线程(与一个管道输入流相关联)
 */
class Consumer extends Thread {

    private PipedInputStream pis;

    public Consumer(PipedInputStream pis) {
        this.pis = pis;
    }

    public void run() {
        try {
            while (true) {
                System.out.println("consumer:" + pis.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}