package com.mylar.lib.base.cryptography.plugins;

import com.mylar.lib.base.cryptography.ICryptography;

import java.security.MessageDigest;

/**
 * 加解密-MD5
 *
 * @author wangz
 * @date 2023/3/19 0019 23:22
 */
public class MD5Cryptography implements ICryptography {

    /**
     * 加密
     *
     * @param text 原文本
     * @return 加密后的文本
     */
    @Override
    public String encrypt(String text) throws Exception {
        return this.encrypt(text, "UTF-8");
    }

    /**
     * 加密
     *
     * @param buffer 二进制对象
     * @return 加密后的文本
     */
    @Override
    public String encrypt(byte[] buffer) throws Exception {
        return this.encrypt(new String(buffer));
    }

    /**
     * 加密
     *
     * @param text 待加密的文本
     * @param key  加密密钥
     * @param iv   加密向量
     * @return 加密后的文本
     */
    @Override
    public String encrypt(String text, String key, String iv) throws Exception {
        return this.encrypt(text);
    }

    /**
     * 加密
     *
     * @param text     原文本
     * @param encoding 编码格式
     * @return 加密后的文本
     */
    @Override
    public String encrypt(String text, String encoding) throws Exception {
        if (encoding == null || encoding.isEmpty()) {
            encoding = "UTF-8";
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] resultByte = text.getBytes(encoding);
        byte[] md5Bytes = md5.digest(resultByte);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = (md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 解密
     *
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    @Override
    public String decrypt(String text) {
        return null;
    }

    /**
     * 解密
     *
     * @param text 待解密的文本
     * @param key  解密密钥
     * @param iv   解密向量
     * @return 解密后的文本
     */
    @Override
    public String decrypt(String text, String key, String iv) {
        return null;
    }

    /**
     * 获取 当前加密算法是否可以解密
     *
     * @return 当前加密算法是否可以解密
     */
    @Override
    public Boolean canDecrypt() {
        return false;
    }
}