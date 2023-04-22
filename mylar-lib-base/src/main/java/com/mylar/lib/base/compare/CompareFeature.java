package com.mylar.lib.base.compare;

/**
 * 比较特性
 *
 * @author wangz
 * @date 2023/4/22 0022 19:48
 */
public enum CompareFeature {

    /**
     * 禁用-循环引用检测（关闭后易出现堆栈溢出）
     */
    DisableCircularReferenceDetect,

    /**
     * 禁用-基类属性字段比较
     */
    DisableSuperDeclaredFieldCompare,
    ;

    CompareFeature() {

    }
}
