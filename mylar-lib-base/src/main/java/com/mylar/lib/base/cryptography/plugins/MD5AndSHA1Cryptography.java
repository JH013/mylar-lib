package com.mylar.lib.base.cryptography.plugins;

import com.mylar.lib.base.cryptography.ICryptography;

/**
 * 加解密-先 MD5 再 SHA1
 *
 * @author wangz
 * @date 2023/3/19 0019 23:26
 */
public class MD5AndSHA1Cryptography implements ICryptography {
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
        String md5Str = new MD5Cryptography().encrypt(text, encoding);
        return new SHA1Cryptography().encrypt(md5Str, encoding);
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
