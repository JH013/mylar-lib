package com.mylar.lib.base.cryptography;

/**
 * 加解密接口
 *
 * @author wangz
 * @date 2023/3/19 0019 23:20
 */
public interface ICryptography {

    /**
     * 加密
     *
     * @param text 原文本
     * @return 加密后的文本
     */
    String encrypt(String text) throws Exception;

    /**
     * 加密
     *
     * @param buffer 二进制对象
     * @return 加密后的文本
     */
    String encrypt(byte[] buffer) throws Exception;

    /**
     * 加密
     *
     * @param text 待加密的文本
     * @param key  加密密钥
     * @param iv   加密向量
     * @return 加密后的文本
     */
    String encrypt(String text, String key, String iv) throws Exception;

    /**
     * 加密
     *
     * @param text     原文本
     * @param encoding 编码格式
     * @return 加密后的文本
     */
    String encrypt(String text, String encoding) throws Exception;

    /**
     * 解密
     *
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    String decrypt(String text) throws Exception;

    /**
     * 解密
     *
     * @param text 待解密的文本
     * @param key  解密密钥
     * @param iv   解密向量
     * @return 解密后的文本
     */
    String decrypt(String text, String key, String iv) throws Exception;

    /**
     * 获取 当前加密算法是否可以解密
     *
     * @return 当前加密算法是否可以解密
     */
    Boolean canDecrypt() throws Exception;
}
