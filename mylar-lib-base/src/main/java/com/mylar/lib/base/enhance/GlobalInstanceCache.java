package com.mylar.lib.base.enhance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局实例缓存
 *
 * @author wangz
 * @date 2023/3/11 0011 18:34
 */
public class GlobalInstanceCache {

    // region 单例

    /**
     * 构造方法
     */
    private GlobalInstanceCache() {
    }

    /**
     * 枚举单例
     *
     * @return 单例
     */
    public static GlobalInstanceCache singleton() {
        return GlobalInstanceCache.SingletonEnum.SINGLETON.instance;
    }

    /**
     * 单例枚举
     */
    private enum SingletonEnum {

        /**
         * 单例
         */
        SINGLETON;

        private final GlobalInstanceCache instance;

        SingletonEnum() {
            instance = new GlobalInstanceCache();
        }
    }

    // endregion

    // region 变量 & 常量

    /**
     * 实例缓存
     */
    private final Map<Class<?>, Object> instanceCache = new HashMap<>();

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(GlobalInstanceCache.class);

    // endregion

    // region 公共方法

    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<? extends T> cl) {
        if (cl.isInterface() || Modifier.isAbstract(cl.getModifiers())) {
            throw new RuntimeException(cl.getName() + " can not be interface or abstract class.");
        }

        T t = (T) instanceCache.get(cl);
        if (t == null) {
            try {
                t = cl.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("{} object instance failed.", cl, e);
                throw new RuntimeException(e);
            }

            instanceCache.put(cl, t);
        }

        return t;
    }

    // endregion
}
