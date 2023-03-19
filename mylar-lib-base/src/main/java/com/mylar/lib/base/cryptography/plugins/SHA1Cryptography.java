package com.mylar.lib.base.cryptography.plugins;

import com.mylar.lib.base.cryptography.ICryptography;

import java.security.MessageDigest;

/**
 * 加解密-SHA1
 *
 * @author wangz
 * @date 2023/3/19 0019 23:25
 */
public class SHA1Cryptography implements ICryptography {

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

        if (text == null || text.length() == 0) {
            return null;
        }

        if (encoding == null || encoding.length() == 0) {
            encoding = "UTF-8";
        }

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
        mdTemp.update(text.getBytes(encoding));

        byte[] md = mdTemp.digest();
        int j = md.length;
        char[] buf = new char[j * 2];
        int k = 0;
        for (byte byte0 : md) {
            buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
            buf[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(buf);
    }

    /**
     * 解密
     *
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    @Override
    public String decrypt(String text) throws Exception {
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
    public String decrypt(String text, String key, String iv) throws Exception {
        return null;
    }

    /**
     * 获取 当前加密算法是否可以解密
     *
     * @return 当前加密算法是否可以解密
     */
    @Override
    public Boolean canDecrypt() throws Exception {
        return false;
    }
}

