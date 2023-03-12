package com.mylar.lib.base.data;

/**
 * 比较日期
 *
 * @author wangz
 * @date 2023/3/12 0012 18:51
 */
public class ComparableDate implements Comparable<ComparableDate> {

    /**
     * 构造方法
     *
     * @param hour   小时
     * @param minute 分钟
     */
    public ComparableDate(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * 小时
     */
    private int hour;

    /**
     * 分钟
     */
    private int minute;

    /**
     * 比较
     *
     * @param dest 目标项
     * @return 比较结果
     */
    @Override
    public int compareTo(ComparableDate dest) {

        // 先比较小时，再比较分钟
        if (this.hour > dest.getHour()) {
            return 1;
        } else if (this.hour == dest.getHour()) {
            return this.minute - dest.getMinute();
        } else {
            return -1;
        }
    }

    /**
     * 格式化
     *
     * @return 结果
     */
    public String format() {
        return String.format("%d:%d", this.hour, this.minute);
    }

    // region getter & setter

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    // endregion
}