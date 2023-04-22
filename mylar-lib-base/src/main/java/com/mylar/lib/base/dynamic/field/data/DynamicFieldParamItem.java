package com.mylar.lib.base.dynamic.field.data;

import java.util.List;

/**
 * 动态字段参数项
 *
 * @author wangz
 * @date 2023/4/22 0022 20:00
 */
public class DynamicFieldParamItem {

    /**
     * 标题
     */
    private String caption;

    /**
     * 水印
     */
    private String watermark;

    /**
     * 字段
     */
    private String field;

    /**
     * 值
     */
    private String value;

    /**
     * 是否允许为空
     */
    private boolean allowEmpty;

    /**
     * 前端控件类型
     */
    private Integer frontControlType;

    /**
     * 前端多选分隔符
     */
    private String frontMultiSplitChar;

    /**
     * 可选集
     */
    private List<DynamicFieldOptionalItem> optionalSet;

    // region getter & setter

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

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

    public boolean isAllowEmpty() {
        return allowEmpty;
    }

    public void setAllowEmpty(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    public Integer getFrontControlType() {
        return frontControlType;
    }

    public void setFrontControlType(Integer frontControlType) {
        this.frontControlType = frontControlType;
    }

    public String getFrontMultiSplitChar() {
        return frontMultiSplitChar;
    }

    public void setFrontMultiSplitChar(String frontMultiSplitChar) {
        this.frontMultiSplitChar = frontMultiSplitChar;
    }

    public List<DynamicFieldOptionalItem> getOptionalSet() {
        return optionalSet;
    }

    public void setOptionalSet(List<DynamicFieldOptionalItem> optionalSet) {
        this.optionalSet = optionalSet;
    }

    // endregion
}
