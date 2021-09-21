package com.mylar.lib.simple.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置-生日
 *
 * @author wangz
 * @date 2021/9/21 0021 19:31
 */
@Component
@ConfigurationProperties(prefix = "birthday")
public class BirthdayConfig {

    /**
     * 年
     */
    private String year;

    /**
     * 月
     */
    private String month;

    /**
     * 日
     */
    private String day;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "BirthdayConfig{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
