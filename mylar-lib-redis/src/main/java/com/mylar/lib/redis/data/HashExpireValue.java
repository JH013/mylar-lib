package com.mylar.lib.redis.data;

import org.apache.commons.lang.StringUtils;

/**
 * 支持 Hash 键过期的 Hash 值
 *
 * @author wangz
 * @date 2023/2/28 0028 0:58
 */
public class HashExpireValue {

    /**
     * 构造方法
     *
     * @param expire    过期时间
     * @param version   版本号
     * @param realValue 真实值
     */
    public HashExpireValue(long expire, String version, String realValue) {
        this.expire = expire;
        this.version = version;
        this.realValue = realValue;
    }

    /**
     * 构造方法
     *
     * @param expire    过期时间
     * @param realValue 真实值
     */
    public HashExpireValue(long expire, String realValue) {
        this.expire = expire;
        this.version = "";
        this.realValue = realValue;
    }

    /**
     * 过期时间（单位：秒）
     */
    private long expire;


    /**
     * 版本号（不能带字符 #）
     */
    private String version;

    /**
     * 真实值
     */
    private String realValue;

    /**
     * 解析 Hash 值
     * <p>
     * 1、数据格式：{时间戳}#{版本号}#{数据}
     *
     * @param originValue 原始值
     * @return 结果
     */
    public static HashExpireValue analyze(String originValue) {
        if (StringUtils.isEmpty(originValue)) {
            return null;
        }

        try {
            int indexFirst = originValue.indexOf('#');
            int indexSecond = originValue.indexOf('#', indexFirst + 1);
            String expire = originValue.substring(0, indexFirst);
            String version = originValue.substring(indexFirst + 1, indexSecond);
            String realValue = originValue.substring(indexSecond + 1);
            return new HashExpireValue(Long.parseLong(expire), realValue, version);
        } catch (Exception e) {
            throw new IllegalArgumentException("Expire hash value analyze failed.", e);
        }
    }

    // region getter & setter

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRealValue() {
        return realValue;
    }

    public void setRealValue(String realValue) {
        this.realValue = realValue;
    }

    // endregion
}
