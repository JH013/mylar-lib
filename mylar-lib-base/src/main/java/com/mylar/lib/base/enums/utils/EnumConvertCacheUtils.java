package com.mylar.lib.base.enums.utils;

import com.mylar.lib.base.enums.core.CodeEnum;
import com.mylar.lib.base.enums.core.NameEnum;
import com.mylar.lib.base.enums.core.ValueEnum;
import com.mylar.lib.base.enums.data.EnumConvertItem;
import com.mylar.lib.base.enums.data.EnumConvertType;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;

/**
 * 枚举转换工具类
 *
 * @author wangz
 * @date 2023/3/20 0020 21:31
 */
public class EnumConvertCacheUtils {

    // region 构造方法

    /**
     * 私有构造方法
     */
    private EnumConvertCacheUtils() {

    }

    // endregion

    // region 变量 & 常量

    /**
     * 枚举转换项-值
     */
    private static final EnumConvertItem valueConvertItem = new EnumConvertItem(ValueEnum.class, o -> ((ValueEnum) o).getValue().toString());

    /**
     * 枚举转换项-编码
     */
    private static final EnumConvertItem codeConvertItem = new EnumConvertItem(CodeEnum.class, o -> ((CodeEnum) o).getCode());

    /**
     * 枚举转换项-名称
     */
    private static final EnumConvertItem nameConvertItem = new EnumConvertItem(NameEnum.class, o -> ((NameEnum) o).getName());

    // endregion

    // region 公共方法

    /**
     * 根据值获取对应的枚举
     *
     * @param valueCodeName 枚举值
     * @param type          枚举类别
     * @param convertType   枚举转换类型
     * @return 枚举对象
     */
    public static <T extends Enum<?>> T convert(String valueCodeName, Class<T> type, EnumConvertType convertType) {
        T enumValue;
        switch (convertType) {
            case VALUE:
                enumValue = valueConvertItem.convertToEnum(valueCodeName, type);
                break;
            case CODE:
                enumValue = codeConvertItem.convertToEnum(valueCodeName, type);
                break;
            default:
                enumValue = nameConvertItem.convertToEnum(valueCodeName, type);
                break;
        }

        return enumValue;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param valueCodeName 枚举值
     * @param type          枚举类别
     * @param convertTypes  枚举转换类型
     * @return 枚举对象
     */
    public static <T extends Enum<?>> T convert(String valueCodeName, Class<T> type, EnumConvertType... convertTypes) {
        T enumValue = null;
        for (EnumConvertType convertType : convertTypes) {
            enumValue = convert(valueCodeName, type, convertType);
            if (enumValue != null) {
                break;
            }
        }

        return enumValue;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param value       枚举值
     * @param type        枚举类别
     * @param convertType 枚举转换类型
     * @return 枚举对象
     */
    public static <T extends Enum<?>> T convert(int value, Class<T> type, EnumConvertType convertType) {
        return convert(Integer.toString(value), type, convertType);
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param value        枚举值
     * @param type         枚举类别
     * @param convertTypes 枚举转换类型
     * @return 枚举对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T convert(String value, Type type, EnumConvertType... convertTypes) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        Class<T> enumClass = ((Class<T>) type);
        if (!enumClass.isEnum()) {
            return null;
        }

        return convert(value, enumClass, convertTypes);
    }

    // endregion
}
