package com.mylar.lib.base.utils;

import com.mylar.lib.base.compare.CompareFeature;
import com.mylar.lib.base.compare.DeepComparator;
import com.mylar.lib.base.compare.ICompareDiff;

/**
 * 比较工具类
 *
 * @author wangz
 * @date 2023/4/22 0022 19:52
 */
public class CompareUtils {

    /**
     * 深度遍历比较
     *
     * @param o1              对象1
     * @param o2              对象2
     * @param compareDiff     差异比较器
     * @param compareFeatures 比较特性
     * @throws IllegalAccessException 异常
     */
    public static void deepCompare(Object o1, Object o2, ICompareDiff compareDiff, CompareFeature... compareFeatures)
            throws IllegalAccessException {
        DeepComparator deepComparator = new DeepComparator(compareDiff);
        deepComparator.addCompareFeatures(compareFeatures);
        deepComparator.compare(o1, o2);
    }
}
