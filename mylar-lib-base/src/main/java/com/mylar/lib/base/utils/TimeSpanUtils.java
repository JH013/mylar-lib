package com.mylar.lib.base.utils;

import com.mylar.lib.base.data.ComparableDate;
import com.mylar.lib.base.data.TimeSpan;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 时间区间工具类
 *
 * @author wangz
 * @date 2023/3/12 0012 19:17
 */
public class TimeSpanUtils {

    /**
     * 构造方法
     */
    private TimeSpanUtils() {

    }

    /**
     * 当前时间是否处于时间区间内
     *
     * @param timeSpan 时间区间
     * @return 结果
     */
    public static boolean currentInTimeSpan(TimeSpan timeSpan) {
        return inTimeSpan(LocalDateTime.now(), timeSpan);
    }

    /**
     * 当前时间是否处于时间区间内
     *
     * @param timeSpans 时间区间集合
     * @return 结果
     */
    public static boolean currentInTimeSpan(List<TimeSpan> timeSpans) {
        return inTimeSpan(LocalDateTime.now(), timeSpans);
    }

    /**
     * 当前时间是否处于时间区间内
     *
     * @param timeSpan 时间区间
     * @return 结果
     */
    public static boolean currentInTimeSpanStr(String timeSpan) {
        return inTimeSpan(LocalDateTime.now(), TimeSpan.convert(timeSpan));
    }

    /**
     * 当前时间是否处于时间区间内
     *
     * @param timeSpans 时间区间集合
     * @return 结果
     */
    public static boolean currentInTimeSpanStr(List<String> timeSpans) {
        if (CollectionUtils.isEmpty(timeSpans)) {
            return false;
        }

        return inTimeSpan(LocalDateTime.now(), timeSpans.stream().map(TimeSpan::convert).collect(Collectors.toList()));
    }

    /**
     * 当前时间是否处于时间区间内
     *
     * @param timeSpans 时间区间集合
     * @return 结果
     */
    public static boolean currentInTimeSpanStr(String[] timeSpans) {
        if (timeSpans == null || timeSpans.length == 0) {
            return false;
        }

        return inTimeSpan(LocalDateTime.now(), Arrays.stream(timeSpans).map(TimeSpan::convert).collect(Collectors.toList()));
    }

    /**
     * 是否处于时间区间内
     *
     * @param dateTime 时间
     * @param timeSpan 时间区间
     * @return 结果
     */
    public static boolean inTimeSpan(LocalDateTime dateTime, TimeSpan timeSpan) {

        // 校验入参
        if (dateTime == null || timeSpan == null) {
            return false;
        }

        // 转换时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
        ComparableDate comparableDate = new ComparableDate(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        // 比较
        return comparableDate.compareTo(timeSpan.getStartTime()) >= 0 && comparableDate.compareTo(timeSpan.getEndTime()) <= 0;
    }

    /**
     * 是否处于时间区间内
     *
     * @param dateTime  时间
     * @param timeSpans 时间区间集合
     * @return 结果
     */
    public static boolean inTimeSpan(LocalDateTime dateTime, List<TimeSpan> timeSpans) {

        // 校验入参
        if (dateTime == null || CollectionUtils.isEmpty(timeSpans)) {
            return false;
        }

        // 转换时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
        ComparableDate comparableDate = new ComparableDate(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        // 循环比较
        for (TimeSpan timeSpan : timeSpans) {
            if (comparableDate.compareTo(timeSpan.getStartTime()) >= 0 && comparableDate.compareTo(timeSpan.getEndTime()) <= 0) {
                return true;
            }
        }

        return false;
    }
}
