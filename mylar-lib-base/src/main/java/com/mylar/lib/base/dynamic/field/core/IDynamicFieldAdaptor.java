package com.mylar.lib.base.dynamic.field.core;

import com.mylar.lib.base.dynamic.field.data.DynamicFieldOptionalItem;
import com.mylar.lib.base.dynamic.field.data.DynamicFiledConvertResult;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 动态字段适配接口
 *
 * @author wangz
 * @date 2023/4/22 0022 20:03
 */
public interface IDynamicFieldAdaptor {

    /**
     * 获取可选集
     *
     * @param field   字段
     * @param explain 字段解释
     * @return 结果
     */
    List<DynamicFieldOptionalItem> getOptionalSet(Field field, DynamicFieldExplain explain);

    /**
     * 转换为真实字段值（String to Object）
     *
     * @param value   字段值
     * @param field   字段
     * @param explain 字段解释
     * @return 结果
     */
    DynamicFiledConvertResult castToReal(String value, Field field, DynamicFieldExplain explain);

    /**
     * 转换字段值为字符串（Object to String）
     *
     * @param value   字段值
     * @param field   字段
     * @param explain 字段解释
     * @return 结果
     */
    String castToString(Object value, Field field, DynamicFieldExplain explain);
}
