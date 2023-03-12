package com.mylar.lib.base.data;

import com.mylar.lib.base.constant.Constants;
import org.apache.commons.lang.StringUtils;

/**
 * 时间区间
 *
 * @author wangz
 * @date 2023/3/12 0012 18:51
 */
public class TimeSpan {

    /**
     * 开始时间
     */
    private ComparableDate startTime;

    /**
     * 结束时间
     */
    private ComparableDate endTime;

    /**
     * 类型转换
     *
     * @param timeSpan 时间区间字符串
     * @return 结果
     */
    public static TimeSpan convert(String timeSpan) {


        // 校验是否为空
        if (StringUtils.isEmpty(timeSpan)) {
            return null;
        }

        // 拆分时间区间
        String[] split = timeSpan.split(Constants.TIME_SPAN_CONNECTOR);
        if (split.length != 2) {
            return null;
        }

        // 校验开始时间格式
        String startTimeStr = split[0];
        if (StringUtils.isEmpty(startTimeStr) || !startTimeStr.matches(Constants.TIME_SPAN_PATTERN)) {
            return null;
        }

        // 校验结束时间格式
        String endTimeStr = split[1];
        if (StringUtils.isEmpty(endTimeStr) || !endTimeStr.matches(Constants.TIME_SPAN_PATTERN)) {
            return null;
        }

        // 格式化
        TimeSpan config = new TimeSpan();
        config.setStartTime(new ComparableDate(Integer.parseInt(startTimeStr.split(":")[0]), Integer.parseInt(startTimeStr.split(":")[1])));
        config.setEndTime(new ComparableDate(Integer.parseInt(endTimeStr.split(":")[0]), Integer.parseInt(endTimeStr.split(":")[1])));
        return config;
    }

    /**
     * 类型转换
     *
     * @param timeSpan 时间区间
     * @return 结果
     */
    public static String convert(TimeSpan timeSpan) {

        // 校验是否为空
        if (timeSpan == null || timeSpan.startTime == null || timeSpan.endTime == null) {
            return null;
        }

        // 格式化
        return String.format("%s%s%s", timeSpan.startTime.format(), Constants.TIME_SPAN_CONNECTOR, timeSpan.endTime.format());
    }

    // region getter & setter

    public ComparableDate getStartTime() {
        return startTime;
    }

    public void setStartTime(ComparableDate startTime) {
        this.startTime = startTime;
    }

    public ComparableDate getEndTime() {
        return endTime;
    }

    public void setEndTime(ComparableDate endTime) {
        this.endTime = endTime;
    }

    // endregion

}