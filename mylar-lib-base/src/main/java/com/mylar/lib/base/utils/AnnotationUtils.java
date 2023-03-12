package com.mylar.lib.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解工具类
 *
 * @author wangz
 * @date 2023/3/11 0011 18:57
 */
public class AnnotationUtils {

    // region 构造方法

    /**
     * 构造方法
     */
    private AnnotationUtils() {

    }

    // endregion

    // region 变量 & 常量

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(AnnotationUtils.class);

    // endregion

    // region 公共方法

    /**
     * 解析属性
     *
     * @param clazz           类型
     * @param annotationClazz 注解类型
     * @return 属性
     */
    public static Map<String, Object> resolveProperties(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        Map<String, Object> map = new HashMap<>();
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(annotationClazz)) {
                map.putAll(AnnotationUtils.resolveProperties(annotation));
            }
        }

        return map;
    }

    /**
     * 解析属性
     *
     * @param annotation 注解
     * @return 属性
     */
    public static Map<String, Object> resolveProperties(Annotation annotation) {
        Map<String, Object> map = new HashMap<>();
        Method[] methods = annotation.annotationType().getDeclaredMethods();
        for (Method method : methods) {
            map.computeIfAbsent(method.getName(), k -> {
                try {
                    return method.invoke(annotation);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(String.format("Resolve annotation %s properties failed.", annotation), e);
                }
            });
        }

        return map;
    }

    // endregion

    // region 私有方法

    // endregion
}
