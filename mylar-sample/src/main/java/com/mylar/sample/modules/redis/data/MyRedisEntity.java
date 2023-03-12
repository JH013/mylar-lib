package com.mylar.sample.modules.redis.data;

/**
 * 自定义实体
 *
 * @author wangz
 * @date 2023/2/26 0026 22:04
 */
public class MyRedisEntity {

    /**
     * 构造方法
     */
    public MyRedisEntity() {

    }

    /**
     * 构造方法
     *
     * @param name  名称
     * @param value 值
     */
    public MyRedisEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    // region getter & setter

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

    // endregion
}
