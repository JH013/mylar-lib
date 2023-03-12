package com.mylar.sample.modules.blocking.queue;

import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author wangz
 * @date 2022/3/1 0001 21:23
 */
public class MyDelayedTask implements Delayed {

    /**
     * 名称
     */
    private final String name;

    /**
     * 延时到期时间
     */
    private final long time;

    /**
     * 构造方法
     *
     * @param name    名称
     * @param seconds 延时时长（单位：秒）
     */
    public MyDelayedTask(String name, long seconds) {
        this.name = name;
        this.time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
    }

    /**
     * 获取剩余延时时长（当前时间 - 延时到期时间）
     *
     * @param unit 单位
     * @return 延时时长
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.time - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 比较排序（当前元素延时到期时间 - 比较对象延时到期时间）
     *
     * @param o 比较对象
     * @return 比较结果
     */
    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return String.format("name: %s, delay time: %s, poll time: %s"
                , this.name, this.time, LocalDateTime.now());
    }
}
