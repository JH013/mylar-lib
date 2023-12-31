package com.mylar.lib.queue.distinct.data;

import com.mylar.lib.base.enums.core.NameEnum;
import com.mylar.lib.base.enums.core.ValueEnum;
import com.mylar.lib.base.enums.data.EnumConvertType;
import com.mylar.lib.base.enums.utils.EnumConvertCacheUtils;

/**
 * 入队状态
 *
 * @author wangz
 * @date 2023/12/27 0027 22:42
 */
public enum EnqueueStatusEnum implements ValueEnum, NameEnum {

    /**
     * 入队异常
     */
    EXCEPTION(-1, "入队异常"),

    /**
     * 未知结果
     */
    UNKNOWN(0, "未知结果"),

    /**
     * 等待中已存在
     */
    WAITING_HAS_EXIST(1, "等待中已存在"),

    /**
     * 添加到等待中
     */
    WAITING_ADD_SUCCESS(2, "添加到等待中"),

    /**
     * 等待中数量超限
     */
    WAITING_OVER_SIZE(3, "等待中数量超限"),

    /**
     * 执行中已存在
     */
    RUNNING_HAS_EXIST(4, "执行中已存在"),

    /**
     * 添加到执行中
     */
    RUNNING_ADD_SUCCESS(5, "添加到执行中"),

    /**
     * 执行中数量超限
     */
    RUNNING_OVER_SIZE(6, "执行中数量超限"),

    ;

    /**
     * 类型
     */
    private final Integer value;

    /**
     * 名称
     */
    private final String name;

    /**
     * 构造方法
     *
     * @param value 值
     * @param name  名称
     */
    EnqueueStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 获取类型
     *
     * @return 类型
     */
    @Override
    public Integer getValue() {
        return this.value;
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
    public static EnqueueStatusEnum create(Integer value) {
        if (value == null) {
            return EnqueueStatusEnum.UNKNOWN;
        }

        EnqueueStatusEnum status = EnumConvertCacheUtils.convert(value, EnqueueStatusEnum.class, EnumConvertType.VALUE);
        if (status == null) {
            return EnqueueStatusEnum.UNKNOWN;
        }

        return status;
    }
}