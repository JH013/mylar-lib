package com.mylar.lib.base.enums.data;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.function.Function;

/**
 * 枚举转换项
 *
 * @author wangz
 * @date 2023/3/20 0020 21:13
 */
public class EnumConvertItem {

    /**
     * 构造方法
     *
     * @param enumInterfaceClazz 枚举接口
     * @param funcGet            取值方法
     */
    public EnumConvertItem(Class<?> enumInterfaceClazz, Function<Object, String> funcGet) {
        this.enumInterfaceClazz = enumInterfaceClazz;
        this.funcGet = funcGet;
    }

    // region 变量

    /**
     * 枚举接口
     */
    private final Class<?> enumInterfaceClazz;

    /**
     * 取值方法
     */
    private final Function<Object, String> funcGet;

    /**
     * 并发控制锁对象。
     */
    private final Object LOCK = new Object();

    /**
     * 枚举项缓存集合
     */
    private final HashMap<Type, HashMap<String, Object>> enumItemsMap = new HashMap<>();

    // endregion

    // region 公共方法

    /**
     * 根据值获取对应的枚举
     *
     * @param value 枚举值
     * @param type  枚举类别
     * @return 枚举值对应的枚举对象
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<?>> T convertToEnum(String value, Class<T> type) {

        // 校验入参
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        // 枚举值映射字典

        // 初始化当前枚举类型的映射字典
        if (!this.enumItemsMap.containsKey(type)) {
            synchronized (this.LOCK) {
                if (!this.enumItemsMap.containsKey(type)) {
                    this.enumItemsMap.put(type, this.formatEnumToMap(type));
                }
            }
        }

        // 根据枚举类型获取映射字典
        HashMap<String, Object> enumValueMap = enumItemsMap.get(type);

        // 从本字典中获取对象
        if (enumValueMap != null && enumValueMap.containsKey(value)) {
            return (T) enumValueMap.get(value);
        }

        // 默认返回空
        return null;
    }

    // endregion

    // region 私有方法

    /**
     * 枚举格式化为字典
     *
     * @param type 枚举类型
     * @param <T>  泛型
     * @return 字典
     */
    private <T extends Enum<?>> HashMap<String, Object> formatEnumToMap(Class<T> type) {
        if (!this.enumInterfaceClazz.isAssignableFrom(type)) {
            return new HashMap<>();
        }

        HashMap<String, Object> map = new HashMap<>();
        for (Object enumObj : type.getEnumConstants()) {

            // 获取枚举值
            String enumValue = this.funcGet.apply(enumObj);

            // 添加映射
            map.put(enumValue, enumObj);
        }

        return map;
    }

    // endregion
}
