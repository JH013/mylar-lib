package com.mylar.lib.base.cryptography;

import com.mylar.lib.base.cryptography.plugins.MD5AndSHA1Cryptography;
import com.mylar.lib.base.cryptography.plugins.MD5Cryptography;
import com.mylar.lib.base.cryptography.plugins.MurMurHashCryptography;
import com.mylar.lib.base.cryptography.plugins.SHA1Cryptography;
import com.mylar.lib.base.data.EncryptionTypeEnum;

/**
 * 加解密管理器
 *
 * @author wangz
 * @date 2023/3/19 0019 23:31
 */
public class CryptographyManager implements ICryptography {

    /**
     * 构造器
     *
     * @param type 加、解密类别
     */
    public CryptographyManager(EncryptionTypeEnum type) {
        switch (type) {
            case MD5:
                this.cryptography = new MD5Cryptography();
                break;
            case MURMUR_HASH:
                this.cryptography = new MurMurHashCryptography();
                break;
            case SHA1:
                this.cryptography = new SHA1Cryptography();
                break;
            case MD5_SHA1:
                this.cryptography = new MD5AndSHA1Cryptography();
                break;
            default:
                throw new RuntimeException("Unknown cryptography type.");
        }
    }

    // region 变量

    /**
     * 加解密对象
     */
    private ICryptography cryptography;

    // endregion

    // region 接口实现

    /**
     * 加密
     *
     * @param text 原文本
     * @return 加密后的文本
     */
    @Override
    public String encrypt(String text) throws Exception {
        return this.cryptography.encrypt(text);
    }

    /**
     * 加密
     *
     * @param buffer 二进制对象
     * @return 加密后的文本
     */
    @Override
    public String encrypt(byte[] buffer) throws Exception {
        return this.cryptography.encrypt(buffer);
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
        return this.cryptography.encrypt(text, key, iv);
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
        return this.cryptography.encrypt(text, encoding);
    }

    /**
     * 解密
     *
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    @Override
    public String decrypt(String text) throws Exception {
        return this.cryptography.decrypt(text);
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
        return this.cryptography.decrypt(text, key, iv);
    }

    /**
     * 获取 当前加密算法是否可以解密
     *
     * @return 当前加密算法是否可以解密
     */
    @Override
    public Boolean canDecrypt() throws Exception {
        return this.cryptography.canDecrypt();
    }

    // endregion
}
