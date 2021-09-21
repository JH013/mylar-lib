package com.mylar.lib.simple.configuration;

/**
 * @author wangz
 * @date 2021/9/21 0021 21:59
 */
public class MyBean {

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    public void init() {
        System.out.println("MyBean init.");
    }

    public void destroy() {
        System.out.println("MyBean destroy.");
    }

    @Override
    public String toString() {
        return "MyBean{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
