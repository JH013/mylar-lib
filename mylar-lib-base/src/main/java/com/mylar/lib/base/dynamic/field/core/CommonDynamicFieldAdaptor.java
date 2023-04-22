package com.mylar.lib.base.dynamic.field.core;

import com.mylar.lib.base.dynamic.field.data.DynamicFieldOptionalItem;
import com.mylar.lib.base.dynamic.field.data.DynamicFiledConvertResult;
import com.mylar.lib.base.enums.core.NameEnum;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用动态字段适配器
 *
 * @author wangz
 * @date 2023/4/22 0022 19:58
 */
public class CommonDynamicFieldAdaptor implements IDynamicFieldAdaptor {

    // region 接口实现

    /**
     * 获取可选集
     *
     * @param field   字段
     * @param explain 字段解释
     * @return 结果
     */
    @Override
    public List<DynamicFieldOptionalItem> getOptionalSet(Field field, DynamicFieldExplain explain) {

        // 可选集
        List<DynamicFieldOptionalItem> optionalItems = new ArrayList<>();

        // 非枚举，直接返回空集合
        Class<?> clazz = field.getType();
        if (!clazz.isEnum()) {
            return optionalItems;
        }

        // 遍历枚举项
        Object[] enumConstants = clazz.getEnumConstants();
        for (Object enumConstant : enumConstants) {

            // 封装可选项
            DynamicFieldOptionalItem optionalItem = this.formatOptionalItem(enumConstant, clazz);
            if (optionalItem != null) {
                optionalItems.add(optionalItem);
            }
        }

        return optionalItems;
    }

    /**
     * 转换为真实字段值（String to Object）
     *
     * @param value   字段值
     * @param field   字段
     * @param explain 字段解释
     * @return 结果
     */
    @Override
    public DynamicFiledConvertResult castToReal(String value, Field field, DynamicFieldExplain explain) {

        // 校验入参
        if (StringUtils.isEmpty(value)) {

            // 允许为空，返回成功
            if (explain.allowEmpty()) {
                return DynamicFiledConvertResult.successItem(null);
            }

            // 字段值为空，返回失败
            return DynamicFiledConvertResult.failedItem(String.format("【%s】值为空", explain.caption()));
        }

        // 获取字段类型
        Class<?> clazz = field.getType();

        // 转换后字段值
        Object castVal = null;
        try {

            // 基础类型
            if (clazz.equals(Byte.class)) {
                castVal = Byte.parseByte(value);
            } else if (clazz.equals(Short.class)) {
                castVal = Short.parseShort(value);
            } else if (clazz.equals(Integer.class)) {
                castVal = Integer.parseInt(value);
            } else if (clazz.equals(Long.class)) {
                castVal = Long.parseLong(value);
            } else if (clazz.equals(Float.class)) {
                castVal = Float.parseFloat(value);
            } else if (clazz.equals(Double.class)) {
                castVal = Double.parseDouble(value);
            } else if (clazz.equals(Boolean.class)) {
                castVal = Boolean.TRUE.toString().equals(value) ? Boolean.TRUE : Boolean.FALSE;
            } else if (clazz.equals(String.class)) {
                castVal = value;
            }
            // 枚举类型
            else if (clazz.isEnum()) {
                Object[] enumConstants = field.getType().getEnumConstants();
                for (Object enumConstant : enumConstants) {
                    if (enumConstant.toString().equals(value)) {
                        castVal = enumConstant;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // TODO: 记录日志
            return DynamicFiledConvertResult.failedItem(String.format("【%s】值转换失败", explain.caption()));
        }

        // 转换后值再次校验是否为空
        if (castVal == null && !explain.allowEmpty()) {
            return DynamicFiledConvertResult.failedItem(String.format("【%s】值为空", explain.caption()));
        }

        // 返回成功
        return DynamicFiledConvertResult.successItem(castVal);
    }

    /**
     * 转换字段值为字符串（Object to String）
     *
     * @param value   字段值
     * @param field   字段
     * @param explain 字段解释
     * @return 结果
     */
    @Override
    public String castToString(Object value, Field field, DynamicFieldExplain explain) {
        return value == null ? "" : value.toString();
    }

    // endregion

    // region 私有方法

    /**
     * 封装可选项
     *
     * @param enumConstant 枚举项
     * @param fieldClazz   字段类型
     * @return 结果
     */
    private DynamicFieldOptionalItem formatOptionalItem(Object enumConstant, Class<?> fieldClazz) {
        try {

            // 标题
            String caption = "";

            // 基于 NameEnum | ValueEnum | CodeEnum 接口实现的枚举
            if (enumConstant instanceof NameEnum) {

                // 获取标题
                Method getCaption = fieldClazz.getDeclaredMethod("getName");
                caption = getCaption.invoke(enumConstant).toString();
            }

            // 获取枚举名称作为值
            String value = enumConstant.toString();
            return new DynamicFieldOptionalItem(caption, value);
        } catch (Exception e) {
            // TODO: 记录日志
            return null;
        }
    }

    // endregion
}