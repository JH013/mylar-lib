package com.mylar.lib.base.dynamic.field.core;

import com.mylar.lib.base.dynamic.field.data.*;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 动态字段转换工具类
 *
 * @author wangz
 * @date 2023/4/22 0022 20:13
 */
public class DynamicFieldResolver {

    /**
     * 转换参数（解析值）
     *
     * @param obj 实体
     * @return 结果
     * @throws IllegalAccessException 异常
     */
    public static DynamicFieldParamBody castParamBody(Object obj) throws Exception {

        // 结果
        DynamicFieldParamBody paramBody = new DynamicFieldParamBody();
        paramBody.setItems(new ArrayList<>());

        // 遍历属性字段
        Class<?> clazz = obj.getClass();
        List<Field> declaredFields = getDeclaredFields(clazz);
        for (Field declaredField : declaredFields) {

            // 属性可见
            declaredField.setAccessible(true);

            /// 获取注解
            DynamicFieldExplain explain = declaredField.getAnnotation(DynamicFieldExplain.class);
            if (explain == null) {
                continue;
            }

            // 封装参数
            DynamicFieldParamItem paramItem = new DynamicFieldParamItem();
            paramItem.setCaption(explain.caption());
            paramItem.setWatermark(explain.watermark());
            paramItem.setField(declaredField.getName());

            // 获取字段值
            Object fieldValue = declaredField.get(obj);
            paramItem.setValue(explain.fieldAdapter().castToString(fieldValue, declaredField, explain));

            // 是否允许为空
            paramItem.setAllowEmpty(explain.allowEmpty());

            // 前端控件类型
            paramItem.setFrontControlType(explain.frontControlType().getValue());

            // 前端多选分隔符
            paramItem.setFrontMultiSplitChar(explain.frontMultiSplitChar());

            // 获取可选集
            List<DynamicFieldOptionalItem> optionalSet = explain.fieldAdapter().getOptionalSet(declaredField, explain);
            paramItem.setOptionalSet(optionalSet);

            // 添加到结果中
            paramBody.addItem(paramItem);
        }

        return paramBody;
    }

    /**
     * 转换参数（不解析值）
     *
     * @param clazz 类型
     * @return 结果
     * @throws IllegalAccessException 异常
     */
    public static DynamicFieldParamBody castParamBody(Class<?> clazz) throws Exception {

        // 结果
        DynamicFieldParamBody paramBody = new DynamicFieldParamBody();
        paramBody.setItems(new ArrayList<>());

        // 遍历属性字段
        List<Field> declaredFields = getDeclaredFields(clazz);
        for (Field declaredField : declaredFields) {

            // 属性可见
            declaredField.setAccessible(true);

            /// 获取注解
            DynamicFieldExplain explain = declaredField.getAnnotation(DynamicFieldExplain.class);
            if (explain == null) {
                continue;
            }

            // 封装参数
            DynamicFieldParamItem paramItem = new DynamicFieldParamItem();
            paramItem.setCaption(explain.caption());
            paramItem.setWatermark(explain.watermark());
            paramItem.setField(declaredField.getName());

            // 是否允许为空
            paramItem.setAllowEmpty(explain.allowEmpty());

            // 前端控件类型
            paramItem.setFrontControlType(explain.frontControlType().getValue());

            // 前端多选分隔符
            paramItem.setFrontMultiSplitChar(explain.frontMultiSplitChar());

            // 获取可选集
            List<DynamicFieldOptionalItem> optionalSet = explain.fieldAdapter().getOptionalSet(declaredField, explain);
            paramItem.setOptionalSet(optionalSet);

            // 添加到结果中
            paramBody.addItem(paramItem);
        }

        return paramBody;
    }

    /**
     * 转换结果
     *
     * @param resultBody 结果体
     * @param clazz      实体类型
     * @return 结果
     * @throws Exception 异常
     */
    public static DynamicFiledConvertResult castResultBody(DynamicFieldResultBody resultBody, Class<?> clazz) throws Exception {

        // 校验入参
        if (CollectionUtils.isEmpty(resultBody.getItems())) {
            return DynamicFiledConvertResult.successItem(null);
        }

        // 实例化对象
        Object obj = clazz.getDeclaredConstructor().newInstance();

        // 遍历属性字段
        List<Field> declaredFields = getDeclaredFields(clazz);
        for (Field declaredField : declaredFields) {

            // 属性可见
            declaredField.setAccessible(true);

            /// 获取注解
            DynamicFieldExplain explain = declaredField.getAnnotation(DynamicFieldExplain.class);
            if (explain == null) {
                continue;
            }

            // 根据属性字段名找到结果
            DynamicFieldResultItem resultItem = resultBody.getItems().stream().filter(t ->
                    declaredField.getName().equals(t.getField())).findFirst().orElse(null);

            if (resultItem == null) {
                continue;
            }

            // 转换属性字段
            DynamicFiledConvertResult convertResult = explain.fieldAdapter().castToReal(resultItem.getValue(), declaredField, explain);

            // 转换失败
            if (!convertResult.isSuccess()) {
                return convertResult;
            }

            // 属性字段赋值
            declaredField.set(obj, convertResult.getValue());
        }

        // 返回成功
        return DynamicFiledConvertResult.successItem(obj);
    }

    /**
     * 获取属性字段
     *
     * @param clazz 当前类型
     * @return 结果
     */
    private static List<Field> getDeclaredFields(Class<?> clazz) {

        // 获取当前类属性字段
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<>(Arrays.asList(declaredFields));

        // 迭代获取基类属性字段
        Class<?> superclass = clazz.getSuperclass();
        while (superclass != null) {
            Field[] superDeclareFields = superclass.getDeclaredFields();
            fields.addAll(Arrays.asList(superDeclareFields));
            superclass = superclass.getSuperclass();
        }

        return fields;
    }
}
