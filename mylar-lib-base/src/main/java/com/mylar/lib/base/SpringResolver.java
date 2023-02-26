package com.mylar.lib.base;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wangz
 * @date 2023/2/26 0026 15:05
 */
@Component
public class SpringResolver implements ApplicationContextAware {
    /**
     * ServletContext对象
     */
    private static ApplicationContext servletContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringResolver.servletContext == null) {
            SpringResolver.servletContext = applicationContext;
        }
    }

    /**
     * Spring Boot对象反转
     *
     * @param type 对象类型
     * @return 反转对象
     */
    public static <T> T resolve(Class<T> type) {
        return resolve(type, null, null);
    }

    /**
     * Spring Boot对象反转
     *
     * @param beanName bean别名
     * @return 反转对象
     */
    public static <T> T resolve(String beanName) {
        return resolve(null, beanName, null);
    }

    /**
     * Spring Boot对象反转
     *
     * @param type     对象类型
     * @param beanName bean别名
     * @return 反转对象
     */
    public static <T> T resolve(Class<T> type, String beanName) {
        return resolve(type, beanName, null);
    }

    /**
     * Spring Boot对象反转
     *
     * @param type 对象类型
     * @param args 构造器参数
     * @return 反转对象
     */
    public static <T> T resolve(Class<T> type, Object... args) {
        return resolve(type, null, args);
    }

    /**
     * Spring Boot对象反转
     *
     * @param type     对象类型
     * @param beanName bean别名
     * @param args     构造器参数
     * @return 反转对象
     */
    public static <T> T resolve(Class<T> type, String beanName, Object... args) {
        try {
            if (StringUtils.hasLength(beanName)) {
                //通过别名获取。
                return servletContext.getBean(beanName, type);
            } else {
                if (null != args && args.length > 0) {
                    //有参构造。
                    return servletContext.getBean(type, args);
                } else {
                    //无参构造。
                    return servletContext.getBean(type);
                }
            }
        } catch (Exception ex) {
//            log.error(String.format("resolve异常:type-%s,name-%s", type, beanName), ex);
            throw ex;
        }
    }

}

