package com.mylar.lib.base.dynamic.field.core;

import com.mylar.lib.base.dynamic.field.data.DynamicFieldAdaptorEnum;
import com.mylar.lib.base.dynamic.field.data.FrontControlTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangz
 * @date 2023/4/22 0022 19:56
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicFieldExplain {

    /**
     * 标题
     *
     * @return 标题
     */
    String caption();

    /**
     * 水印
     *
     * @return 水印
     */
    String watermark() default "";

    /**
     * 是否允许为空
     *
     * @return 是否允许为空
     */
    boolean allowEmpty() default false;

    /**
     * 前端控件类型
     *
     * @return 前端控件类型
     */
    FrontControlTypeEnum frontControlType() default FrontControlTypeEnum.SIMPLE_INPUT;

    /**
     * 前端多选分隔符
     *
     * @return 前端多选分隔符
     */
    String frontMultiSplitChar() default ",";

    /**
     * 动态字段适配枚举
     *
     * @return 动态字段适配枚举
     */
    DynamicFieldAdaptorEnum fieldAdapter() default DynamicFieldAdaptorEnum.COMMON;
}
