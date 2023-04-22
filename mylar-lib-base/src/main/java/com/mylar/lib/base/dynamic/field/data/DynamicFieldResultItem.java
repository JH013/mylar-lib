package com.mylar.lib.base.dynamic.field.data;

/**
 * 动态字段结果项
 *
 * @author wangz
 * @date 2023/4/22 0022 20:01
 */
public class DynamicFieldResultItem {

    /**
     * 字段
     */
    private String field;

    /**
     * 值
     */
    private String value;

    // region getter & setter

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // endregion
}
