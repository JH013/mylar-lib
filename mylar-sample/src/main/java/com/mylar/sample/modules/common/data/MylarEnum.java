package com.mylar.sample.modules.common.data;

import com.alibaba.fastjson.annotation.JSONType;
import com.mylar.lib.base.enums.core.CodeEnum;
import com.mylar.lib.base.enums.core.NameEnum;
import com.mylar.lib.base.enums.core.ValueEnum;
import com.mylar.lib.base.enums.data.EnumConvertType;
import com.mylar.lib.base.enums.serialization.EnumCodeValueDeserializer;
import com.mylar.lib.base.enums.utils.EnumConvertCacheUtils;

/**
 * Mylar 枚举
 *
 * @author wangz
 * @date 2023/3/20 0020 21:53
 */
@JSONType(deserializer = EnumCodeValueDeserializer.class)
public enum MylarEnum implements ValueEnum, NameEnum, CodeEnum {

    /**
     * 系统设置
     */
    SYSTEM_SETTINGS(0, "系统设置", "SYSTEM SETTINGS"),

    /**
     * 用户定义
     */
    USER_DEFINITION(1, "用户定义", "USER DEFINITION"),

    /**
     * 自动生成
     */
    AUTO_GENERATION(2, "自动生成", "AUTO GENERATION"),

    ;

    /**
     * 构造方法
     *
     * @param name  名称
     * @param value 值
     */
    MylarEnum(Integer value, String name, String code) {
        this.name = name;
        this.value = value;
        this.code = code;
    }

    /**
     * 值
     */
    private final Integer value;

    /**
     * 名称
     */
    private final String name;

    /**
     * 编码
     */
    private final String code;

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * 根据值获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static MylarEnum create(Integer value) {
        return EnumConvertCacheUtils.convert(value, MylarEnum.class, EnumConvertType.VALUE);
    }
}
