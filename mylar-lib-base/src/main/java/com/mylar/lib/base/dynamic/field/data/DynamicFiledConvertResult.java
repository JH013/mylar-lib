package com.mylar.lib.base.dynamic.field.data;

/**
 * 动态字段转换结果
 *
 * @author wangz
 * @date 2023/4/22 0022 20:01
 */
public class DynamicFiledConvertResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 字段值
     */
    private Object value;

    /**
     * 成功
     *
     * @param value 字段值
     * @return 结果
     */
    public static DynamicFiledConvertResult successItem(Object value) {
        DynamicFiledConvertResult result = new DynamicFiledConvertResult();
        result.setSuccess(true);
        result.setValue(value);
        return result;
    }

    /**
     * 失败
     *
     * @param message 错误信息
     * @return 结果
     */
    public static DynamicFiledConvertResult failedItem(String message) {
        DynamicFiledConvertResult result = new DynamicFiledConvertResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    // region getter & setter

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    // endregion
}
