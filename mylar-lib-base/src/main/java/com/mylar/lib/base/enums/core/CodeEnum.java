package com.mylar.lib.base.enums.core;

/**
 * 枚举接口-名称
 * <p>
 * 说明：
 * 1、实现此接口的枚举可通过 {@link com.mylar.lib.base.enums.utils.EnumConvertCacheUtils} 和枚举值获取枚举对象
 * 2、枚举上添加注解 {@link com.mylar.lib.base.enums.serialization.EnumCodeValueDeserializer} 以支持 fastjson 反序列化
 * 3、接口优先级 {@link ValueEnum} > {@link CodeEnum} > {@link NameEnum}
 *
 * @author wangz
 * @date 2023/3/20 0020 21:09
 */
public interface CodeEnum {

    /**
     * 获取编码
     *
     * @return 编码
     */
    String getCode();
}
