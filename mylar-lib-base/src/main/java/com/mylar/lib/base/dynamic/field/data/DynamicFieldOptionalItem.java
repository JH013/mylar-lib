package com.mylar.lib.base.dynamic.field.data;

/**
 * 动态字段可选项
 *
 * @author wangz
 * @date 2023/4/22 0022 19:59
 */
public class DynamicFieldOptionalItem {

    /**
     * 构造方法
     *
     * @param caption 标题
     * @param value   值
     */
    public DynamicFieldOptionalItem(String caption, String value) {
        this.caption = caption;
        this.value = value;
    }

    /**
     * 标题
     */
    private String caption;

    /**
     * 值
     */
    private String value;

    // region getter & setter

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
