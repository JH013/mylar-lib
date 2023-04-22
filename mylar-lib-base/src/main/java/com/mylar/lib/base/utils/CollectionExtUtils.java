package com.mylar.lib.base.utils;

import java.util.Collection;
import java.util.List;

/**
 * 集合扩展工具类
 *
 * @author wangz
 * @date 2023/4/22 0022 19:45
 */
public class CollectionExtUtils {

    /**
     * 是否为空
     *
     * @param list 集合
     * @param <T>  泛型
     * @return 结果
     */
    public static <T> boolean isBlank(List<T> list) {
        return (null == list || list.isEmpty());
    }

    /**
     * 是否不为空
     *
     * @param list 集合
     * @param <T>  泛型
     * @return 结果
     */
    public static <T> boolean isNotBlank(List<T> list) {
        return !isBlank(list);
    }

    /**
     * 是否为空
     *
     * @param collection 集合
     * @return 结果
     */
    public static boolean isBlank(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 是否不为空
     *
     * @param collection 集合
     * @return 结果
     */
    public static boolean isNotBlank(Collection<?> collection) {
        return !isBlank(collection);
    }
}
