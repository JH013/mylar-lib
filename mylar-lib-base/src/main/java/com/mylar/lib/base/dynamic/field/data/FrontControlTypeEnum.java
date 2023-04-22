package com.mylar.lib.base.dynamic.field.data;

import com.mylar.lib.base.enums.core.NameEnum;
import com.mylar.lib.base.enums.core.ValueEnum;
import com.mylar.lib.base.enums.data.EnumConvertType;
import com.mylar.lib.base.enums.utils.EnumConvertCacheUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 前端控件类型枚举
 *
 * @author wangz
 * @date 2023/4/22 0022 20:04
 */
public enum FrontControlTypeEnum implements ValueEnum, NameEnum {

    /**
     * 简单输入
     */
    SIMPLE_INPUT(0, "简单输入"),

    /**
     * 单选
     */
    SINGLE_CHOICE(1, "单选"),

    /**
     * 多选
     */
    MULTIPLE_CHOICE(2, "多选"),

    ;

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 名称
     */
    private final String name;

    /**
     * 构造方法
     *
     * @param type 类型
     * @param name 名称
     */
    FrontControlTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    @Override
    public Integer getValue() {
        return this.type;
    }

    /**
     * 获取名称
     *
     * @return 名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 根据值获取对应的枚举。
     *
     * @param value 值
     * @return 对应的枚举
     */
    public static FrontControlTypeEnum create(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return EnumConvertCacheUtils.convert(value, FrontControlTypeEnum.class, EnumConvertType.VALUE);
    }
}
