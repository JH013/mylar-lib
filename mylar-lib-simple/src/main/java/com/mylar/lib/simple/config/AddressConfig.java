package com.mylar.lib.simple.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 配置-地址
 *
 * @author wangz
 * @date 2021/9/21 0021 19:23
 */
@Component
public class AddressConfig {

    /**
     * 国家
     */
    @Value("${address.country}")
    private String country;

    /**
     * 省份
     */
    @Value("${address.province}")
    private String province;

    /**
     * 城市
     */
    @Value("${address.city}")
    private String city;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "AddressConfig{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
