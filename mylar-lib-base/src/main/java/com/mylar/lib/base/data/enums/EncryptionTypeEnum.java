package com.mylar.lib.base.data.enums;

/**
 * 加解密类型
 *
 * @author wangz
 * @date 2023/3/19 0019 23:28
 */
public enum EncryptionTypeEnum {

    /**
     * AES加解密
     */
    AES("AES加解密", 0),
    /**
     * MD5通用加密
     */
    MD5("MD5通用加密", 1),
    /**
     * MD5Hash加密D
     */
    MURMUR_HASH("MurMur一致性Hash非加密散列", 2),
    /**
     * SHA1加密
     */
    SHA1("SHA1加密", 3),
    /**
     * MD5_SHA1加密
     */
    MD5_SHA1("MD5_SHA1加密", 4),

    ;

    /**
     * 标题
     */
    private final String caption;
    /**
     * 枚举值
     */
    private final Integer value;

    /**
     * 构造器
     *
     * @param caption 标题
     * @param value   枚举值
     */
    EncryptionTypeEnum(String caption, Integer value) {
        this.caption = caption;
        this.value = value;
    }

    /**
     * 获取标题
     *
     * @return 标题
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * 枚举值字符串
     *
     * @return S
     */
    @Override
    public String toString() {
        return this.getValue().toString();
    }
}
