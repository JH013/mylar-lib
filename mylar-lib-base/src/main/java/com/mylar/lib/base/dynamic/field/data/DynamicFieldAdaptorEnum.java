package com.mylar.lib.base.dynamic.field.data;

import com.mylar.lib.base.dynamic.field.core.CommonDynamicFieldAdaptor;
import com.mylar.lib.base.dynamic.field.core.DynamicFieldExplain;
import com.mylar.lib.base.dynamic.field.core.IDynamicFieldAdaptor;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 动态字段适配枚举
 *
 * @author wangz
 * @date 2023/4/22 0022 20:06
 */
public enum DynamicFieldAdaptorEnum implements IDynamicFieldAdaptor {

    /**
     * 通用
     */
    COMMON {
        /**
         * 获取可选集
         */
        @Override
        public List<DynamicFieldOptionalItem> getOptionalSet(Field field, DynamicFieldExplain explain) {
            return new CommonDynamicFieldAdaptor().getOptionalSet(field, explain);
        }

        /**
         * 转换为真实字段值（String to Object）
         */
        @Override
        public DynamicFiledConvertResult castToReal(String value, Field field, DynamicFieldExplain explain) {
            return new CommonDynamicFieldAdaptor().castToReal(value, field, explain);
        }

        /**
         * 转换字段值为字符串（Object to String）
         */
        @Override
        public String castToString(Object value, Field field, DynamicFieldExplain explain) {
            return new CommonDynamicFieldAdaptor().castToString(value, field, explain);
        }
    },

    ;
}
