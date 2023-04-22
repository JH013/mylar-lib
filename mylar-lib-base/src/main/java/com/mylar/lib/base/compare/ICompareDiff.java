package com.mylar.lib.base.compare;

import java.lang.reflect.Field;

/**
 * 差异比较器接口
 *
 * @author wangz
 * @date 2023/4/22 0022 19:50
 */
public interface ICompareDiff {

    /**
     * 是否忽略此字段比较
     *
     * @param field 字段
     * @return 是否忽略
     */
    boolean ignore(Field field);

    /**
     * 是否继续递归比较属性字段
     *
     * @param field 字段
     * @param o1    对象1
     * @param o2    对象2
     * @return 是否继续
     */
    boolean continueCompareFields(Field field, Object o1, Object o2);

    /**
     * 执行比较
     *
     * @param field 字段
     * @param o1    对象1
     * @param o2    对象2
     */
    void doCompare(Field field, Object o1, Object o2);
}
