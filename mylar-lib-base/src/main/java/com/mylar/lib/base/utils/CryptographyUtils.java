package com.mylar.lib.base.utils;

import com.mylar.lib.base.cryptography.CryptographyManager;
import com.mylar.lib.base.data.EncryptionTypeEnum;

import java.nio.charset.StandardCharsets;

/**
 * 加解密工具类
 *
 * @author wangz
 * @date 2023/3/19 0019 23:18
 */
public class CryptographyUtils {

    /**
     * 字符串生成一致性 Hash
     *
     * @param text 字符串
     * @return Hash
     */
    public static long murMurHash(String text) {
        try {
            return murMurHash(text.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new RuntimeException(String.format("%s,hash异常", text), ex);
        }
    }

    /**
     * 二进制对象生成一致性 Hash
     *
     * @param buffer 二进制数据
     * @return Hash
     */
    public static long murMurHash(byte[] buffer) throws Exception {
        CryptographyManager cm = new CryptographyManager(EncryptionTypeEnum.MURMUR_HASH);
        return Long.parseLong(cm.encrypt(buffer));
    }

    /**
     * 获取哈希
     *
     * @param code code
     * @return 哈希结果
     */
    public static long getHash(String code) {
        try {
            return murMurHash(code);
        } catch (Exception e) {
            // log.error("哈希失败", e);
            return 0;
        }
    }
}
