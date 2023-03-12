package com.mylar.lib.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mylar.lib.base.data.LambdaExpressionObject;
import org.springframework.util.StringUtils;

/**
 * @author wangz
 * @date 2023/2/26 0026 18:52
 */
public class JsonUtils {

    /**
     * 构造方法
     */
    private JsonUtils() {
    }

    // region 变量

    /**
     * 特性
     */
    public static final SerializerFeature[] DEFAULT_SERIALIZER_FEATURES = new SerializerFeature[]{
            SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNonStringKeyAsString,
            SerializerFeature.WriteNullListAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteDateUseDateFormat,
    };

    // endregion

    // region 公共方法

    /**
     * 序列化
     *
     * @param obj 对象
     * @return 结果
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof String) {
            return obj.toString();
        }

        return JSON.toJSONString(obj, DEFAULT_SERIALIZER_FEATURES);
    }

    /**
     * 反序列化
     *
     * @param json json
     * @param cls  类型
     * @return 结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T deJson(String json, Class<T> cls) {
        if (!StringUtils.hasLength(json)) {
            return null;
        }

        if (cls == String.class) {
            return (T) json;
        }

        LambdaExpressionObject<T> object = new LambdaExpressionObject<T>();
        object.setObj(JSON.parseObject(json, cls));
        return object.getObj();
    }

    /**
     * 反序列化
     *
     * @param json json
     * @param type 类型
     * @return 结果
     */
    public static <T> T deJson(String json, TypeReference<T> type) {
        if (!StringUtils.hasLength(json)) {
            return null;
        }

        LambdaExpressionObject<T> object = new LambdaExpressionObject<T>();
        object.setObj(JSON.parseObject(json, type));
        return object.getObj();
    }

    /**
     * 反序列化
     *
     * @param json json
     * @return 结果
     */
    public static JSONObject deJson(String json) {
        return JSON.parseObject(json);
    }

    // endregion
}
