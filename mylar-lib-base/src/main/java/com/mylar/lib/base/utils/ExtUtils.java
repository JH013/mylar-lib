package com.mylar.lib.base.utils;

/**
 * 扩展工具类
 *
 * @author wangz
 * @date 2023/2/26 0026 22:01
 */
public class ExtUtils {

    /**
     * 构造方法
     */
    private ExtUtils() {

    }

    /**
     * 获取随机值
     *
     * @param begin 开始值
     * @param end   结束值
     * @return 结果
     */
    public static int randomInt(int begin, int end) {
        return begin + (int) (Math.random() * (end - begin));
    }
}
