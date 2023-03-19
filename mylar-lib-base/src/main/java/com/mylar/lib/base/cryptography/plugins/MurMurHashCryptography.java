package com.mylar.lib.base.cryptography.plugins;

import com.mylar.lib.base.cryptography.ICryptography;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 加解密-一致性哈希
 *
 * @author wangz
 * @date 2023/3/19 0019 23:24
 */
public class MurMurHashCryptography implements ICryptography {

    /**
     * 加密
     *
     * @param text 原文本
     * @return 加密后的文本
     */
    @Override
    public String encrypt(String text) throws Exception {
        return this.encrypt(text.getBytes("utf-8"));
    }

    /**
     * 加密
     *
     * @param buffer 二进制对象
     * @return 加密后的文本
     */
    @Override
    public String encrypt(byte[] buffer) throws Exception {
        if (null == buffer) {
            return null;
        }

        ByteBuffer buf = ByteBuffer.wrap(buffer);
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return Long.toString(h);
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
        return this.encrypt(text.getBytes(encoding));
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
