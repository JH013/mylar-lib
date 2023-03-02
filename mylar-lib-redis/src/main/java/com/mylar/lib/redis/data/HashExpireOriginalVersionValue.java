package com.mylar.lib.redis.data;

/**
 * 支持 Hash 键过期的 Hash 值（附带原始版本号）
 *
 * @author wangz
 * @date 2023/3/3 0003 1:52
 */
public class HashExpireOriginalVersionValue extends HashExpireValue {

    /**
     * 构造方法
     *
     * @param expire          过期时间
     * @param version         版本号
     * @param originalVersion 原始版本号
     * @param realValue       真实值
     */
    public HashExpireOriginalVersionValue(long expire, String version, String originalVersion, String realValue) {
        super(expire, version, realValue);
        this.originalVersion = originalVersion;
    }

    /**
     * 原始版本号
     */
    private String originalVersion;

    // region getter & setter

    public String getOriginalVersion() {
        return originalVersion;
    }

    public void setOriginalVersion(String originalVersion) {
        this.originalVersion = originalVersion;
    }

    // endregion
}
