package com.mylar.lib.base.utils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 对象扩展工具类
 *
 * @author wangz
 * @date 2023/4/22 0022 19:47
 */
public class ObjectExtUtils {

    /**
     * 对象是否相等
     *
     * @param src  对象1
     * @param dest 对象2
     * @param <T>  泛型
     * @return 是否相等
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean isEquals(T src, T dest) {

        if (src == null && dest == null) {
            return true;
        }

        // 非空对象
        T notNullObj = src == null ? dest : src;

        // 是否存在空值
        boolean existNull = src == null || dest == null;

        // 处理集合，集合无元素等同于对象为空
        if (notNullObj instanceof Collection) {
            if (CollectionExtUtils.isBlank((Collection<?>) notNullObj) && existNull) {
                return true;
            }

            return JSON.toJSONString(src).equals(JSON.toJSONString(dest));
        }

        // 处理数组，数组无元素等同于对象为空
        if (notNullObj.getClass().isArray()) {
            if (Array.getLength(notNullObj) == 0 && existNull) {
                return true;
            }

            return JSON.toJSONString(src).equals(JSON.toJSONString(dest));
        }

        // 对象为空时生成默认值
        if (src == null) {
            src = (T) ObjectExtUtils.getBasicClassDefaultVal(dest);
        }

        // 对象为空时生成默认值
        if (dest == null) {
            dest = (T) ObjectExtUtils.getBasicClassDefaultVal(src);
        }

        return ObjectExtUtils.nullSafeEquals(src, dest);
    }

    /**
     * 对象是否相等（空值安全）
     *
     * @param o1 对象1
     * @param o2 对象 2
     * @return 是否相等
     */
    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return arrayEquals(o1, o2);
        }
        return false;
    }

    /**
     * 数组是否相等
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return 是否相等
     */
    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        }
        if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        }
        if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        }
        if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        }
        if (o1 instanceof short[] && o2 instanceof short[]) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }
        return false;
    }

    /**
     * 对象是否为空
     *
     * @param obj 对象
     * @return 是否为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return false;
    }

    /**
     * 获取默认值（空值安全）
     *
     * @param value        原值
     * @param defaultValue 默认值
     * @return 结果
     */
    public static Object emptySafeGetDefaultValue(Object value, Object defaultValue) {
        return ObjectExtUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * 获取默认对象
     *
     * @param obj   对象
     * @param clazz 类型
     * @return 默认值
     */
    public static Object nullSafeGetDefaultValue(Object obj, Class clazz) throws IllegalAccessException, InstantiationException {
        if (null != obj) {
            return obj;
        }

        return clazz.newInstance();
    }

    /**
     * 非基础类型
     *
     * @param obj 对象
     * @return 结果
     */
    public static boolean notBaseType(Object obj) {

        return !(obj instanceof Byte)
                && !(obj instanceof Short)
                && !(obj instanceof Integer)
                && !(obj instanceof Long)
                && !(obj instanceof Float)
                && !(obj instanceof Double)
                && !(obj instanceof BigDecimal)
                && !(obj instanceof Character)
                && !(obj instanceof Boolean)
                && !(obj instanceof String)
                && !(obj instanceof Date)
                && !(obj instanceof Enum)
                && !(obj instanceof Collection)
                && !(obj.getClass().isArray());
    }

    /**
     * 基础类型
     *
     * @param obj 对象
     * @return 结果
     */
    public static boolean isBaseType(Object obj) {

        return (obj instanceof Byte)
                || (obj instanceof Short)
                || (obj instanceof Integer)
                || (obj instanceof Long)
                || (obj instanceof Float)
                || (obj instanceof Double)
                || (obj instanceof BigDecimal)
                || (obj instanceof Character)
                || (obj instanceof Boolean)
                || (obj instanceof String)
                || (obj instanceof Date)
                || (obj instanceof Collection)
                || (obj.getClass().isArray());
    }


    /**
     * 获取基础类型默认值
     *
     * @param notNullObj 非空对象
     * @param <T>        泛型
     * @return 默认值
     */
    public static <T> Object getBasicClassDefaultVal(T notNullObj) {
        if (notNullObj instanceof String) {
            return "";
        }

        if (notNullObj instanceof BigDecimal) {
            return BigDecimal.ZERO;
        }

        if (notNullObj instanceof Byte) {
            return (byte) 0;
        }

        if (notNullObj instanceof Integer) {
            return 0;
        }

        if (notNullObj instanceof Long) {
            return 0L;
        }

        if (notNullObj instanceof Float) {
            return 0F;
        }

        if (notNullObj instanceof Double) {
            return 0D;
        }

        return null;
    }
}
