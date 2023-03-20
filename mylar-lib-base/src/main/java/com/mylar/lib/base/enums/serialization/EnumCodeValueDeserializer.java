package com.mylar.lib.base.enums.serialization;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.mylar.lib.base.enums.data.EnumConvertType;
import com.mylar.lib.base.enums.utils.EnumConvertCacheUtils;

import java.lang.reflect.Type;

/**
 * fastJson 枚举反序列化器
 * <p>
 * 使用示例：枚举类添加注解 @JSONType(deserializer = EnumCodeValueDeserializer.class)
 *
 * @author wangz
 * @date 2023/3/20 0020 21:46
 */
public class EnumCodeValueDeserializer implements ObjectDeserializer {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.lexer;
        final int token = lexer.token();
        String value = null;
        T enumObject = null;
        if (token == JSONToken.LITERAL_INT) {
            value = lexer.numberString();
            lexer.nextToken(JSONToken.COMMA);
            enumObject = (T) EnumConvertCacheUtils.convert(value, type, EnumConvertType.VALUE, EnumConvertType.VALUE);
        } else if (token == JSONToken.LITERAL_STRING) {
            value = lexer.stringVal();
            lexer.nextToken(JSONToken.COMMA);
            enumObject = (T) EnumConvertCacheUtils.convert(value, type, EnumConvertType.CODE, EnumConvertType.NAME);
        }

        return enumObject;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}